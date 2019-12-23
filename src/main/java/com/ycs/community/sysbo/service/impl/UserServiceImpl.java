package com.ycs.community.sysbo.service.impl;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.basebo.utils.BeanUtil;
import com.ycs.community.basebo.utils.PageUtil;
import com.ycs.community.coobo.utils.FileUtil;
import com.ycs.community.spring.exception.BadRequestException;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.spring.security.utils.SecurityUtil;
import com.ycs.community.sysbo.dao.DeptDao;
import com.ycs.community.sysbo.dao.JobDao;
import com.ycs.community.sysbo.dao.RoleDao;
import com.ycs.community.sysbo.dao.UserDao;
import com.ycs.community.sysbo.domain.dto.QryUserPageRequestDto;
import com.ycs.community.sysbo.domain.dto.QryUserPageResponseDto;
import com.ycs.community.sysbo.domain.dto.UserRequestDto;
import com.ycs.community.sysbo.domain.po.DeptPo;
import com.ycs.community.sysbo.domain.po.JobPo;
import com.ycs.community.sysbo.domain.po.RolePo;
import com.ycs.community.sysbo.domain.po.UserPo;
import com.ycs.community.sysbo.service.UserService;
import com.ycs.community.sysbo.utils.EncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private DeptDao deptDao;
    @Autowired
    private JobDao jobDao;
    @Value("${file.avatar}")
    private String avatarPath;
    @Value("${file.max-size}")
    private long maxSize;

    @Override
    public boolean addOrUpdateUser(UserPo userPo) {
        // 查询数据库用户是否存在
        UserPo user = userDao.qryUserById(userPo.getId());
        if(StringUtils.isEmpty(user)) {
            // 设置默认密码
            String encryptDefaultPassword = EncryptUtil.encryptDefaultPassword();
            userPo.setPassword(encryptDefaultPassword);
            userPo.setEnabled(true);
            userPo.setCreTm(new Date().getTime());
            int result = userDao.addUser(userPo);
            if (result != 1) {
                throw new CustomizeBusinessException(HiMsgCdConstants.ADD_USER_FAIL, "添加用户失败");
            }
        } else {
            user.setName(userPo.getName());
            user.setAvatar(userPo.getAvatar());
            user.setUpdTm(new Date().getTime());
            int result = userDao.updUser(user);
            if (result != 1) {
                throw new CustomizeBusinessException(HiMsgCdConstants.UPD_USER_FAIL, "更新用户失败");
            }
        }

        return true;
    }

    @Override
    public UserPo qryUserById(long id) {
        UserPo userPo = userDao.qryUserById(id);
        if (!StringUtils.isEmpty(userPo)) {
            // 根据用户ID获取角色
            List<RolePo> roleList = roleDao.qryRolesByUserId(userPo.getId());
            userPo.setRoles(roleList);

            // 根据部门ID获取部门
            DeptPo deptPo = deptDao.qryDeptById(userPo.getDeptId());
            userPo.setDept(deptPo);

            // 根据岗位ID获取岗位
            JobPo jobPo = jobDao.qryJobById(userPo.getJobId());
            userPo.setJob(jobPo);
            return userPo;
        } else {
            throw new CustomizeBusinessException(HiMsgCdConstants.USER_NOT_EXIST, "用户不存在");
        }
    }

    @Override
    public UserPo qryUserInfoByName(String userNm) {
        UserPo userPo = userDao.qryUserInfoByName(userNm);
        if (StringUtils.isEmpty(userPo)) {
            throw new CustomizeBusinessException(HiMsgCdConstants.USER_NOT_EXIST, "用户不存在");
        }
        return userPo;
    }

    @Override
    public List<RolePo> qryRolesByUserId(Long userId) {
        List<RolePo> roleList = roleDao.qryRolesByUserId(userId);
        if (!CollectionUtils.isEmpty(roleList)) {
            return roleList;
        }
        return null;
    }

    @Override
    public QryUserPageResponseDto qryUserPage(QryUserPageRequestDto request) {
        Map<String, Object> paramMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(request.getDeptIds())) {
            paramMap.put("deptIds", request.getDeptIds());
        }
        paramMap.put("name", request.getName());
        if (!StringUtils.isEmpty(request.getStartTime())) {
            paramMap.put("startTime", request.getStartTime().getTime());
        }
        if (!StringUtils.isEmpty(request.getEndTime())) {
            paramMap.put("endTime", request.getEndTime().getTime());
        }
        paramMap.put("enabled", request.getEnabled());
        // 递归获取部门下的所有子部门
        if (!StringUtils.isEmpty(request.getDeptId())) {
            List<Long> deptIds = new ArrayList<>();
            this.buildDeptIds(request.getDeptId(), deptIds);
            paramMap.put("deptIds", deptIds);
        }
        // 查询总条数
        int totalCount = userDao.qryUserCount(paramMap);
        // 计算分页信息
        PageUtil.calculatePageInfo(totalCount, request.getCurrentPage(), request.getPageSize());
        // 分页查询
        paramMap.put("startRow", PageUtil.getStartRow());
        paramMap.put("offset", PageUtil.getPageSize());
        List<UserPo> data = userDao.qryUserPage(paramMap);
        // 查询用户对应角色
        data.forEach(userPo -> {
            List<RolePo> roles = roleDao.qryRolesByUserId(userPo.getId());
            userPo.setRoles(roles);
        });
        // 查询用户对应部门
        data.forEach(userPo -> {
            DeptPo dept = deptDao.qryDeptByUserId(userPo.getId());
            userPo.setDept(dept);
        });
        // 查询用户对应岗位
        data.forEach(userPo -> {
            JobPo job = jobDao.qryJobByUserId(userPo.getId());
            userPo.setJob(job);
        });
        // 组装分页信息
        QryUserPageResponseDto response = new QryUserPageResponseDto();
        if (!CollectionUtils.isEmpty(data)) {
            response.setData(data);
            response.setTotal(totalCount);
        }
        return response;
    }

    /**
     * 递归获取所有子部门ID
     * @param pid
     * @param deptIds
     * @return
     */
    private List<Long> buildDeptIds(Long pid, List<Long> deptIds) {
        deptIds.add(pid);
        List<DeptPo> deptPoList = deptDao.qryDeptsByPid(pid);
        deptPoList.forEach(deptPo -> {
            List<DeptPo> children = deptDao.qryDeptsByPid(deptPo.getId());
            if (!CollectionUtils.isEmpty(children)) {
                children.forEach(deptPo1 -> {
                    buildDeptIds(deptPo1.getId(), deptIds);
                });
            } else {
                deptIds.add(deptPo.getId());
            }
        });
        return deptIds;
    }

    @Override
    @Transactional(rollbackFor = {CustomizeBusinessException.class})
    public boolean addUser(UserRequestDto request) {
        // 添加用户基本信息
        UserPo userPo = BeanUtil.trans2Entity(request, UserPo.class);
        // 设置默认密码
        String encryptDefaultPassword = EncryptUtil.encryptDefaultPassword();
        userPo.setPassword(encryptDefaultPassword);
        userPo.setDeptId(userPo.getDept().getId());
        userPo.setJobId(userPo.getJob().getId());
        userPo.setCreTm(new Date().getTime());
        if (userDao.addUser(userPo) < 1) {
            throw new CustomizeBusinessException(HiMsgCdConstants.ADD_USER_FAIL, "添加用户失败");
        } else {
            // 添加用户基本信息前先添加用户角色
            List<RolePo> roles = request.getRoles();
            List<Long> addList = new ArrayList<>(); // 新增角色列表
            addList = roles.stream().map(RolePo :: getId).collect(Collectors.toList());
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("userId", userPo.getId()); // 获取添加用户ID
            paramMap.put("roles", addList);
            if (!CollectionUtils.isEmpty(addList)) {
                paramMap.put("roles", addList);
                if (userDao.addUserRoles(paramMap) < 1) {
                    throw new CustomizeBusinessException(HiMsgCdConstants.ADD_USER_ROLE_FAIL, "添加用户角色失败");
                }
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {CustomizeBusinessException.class})
    public boolean delUser(Long id) {
        // 删除用户前先删除用户的角色
        if (roleDao.delRolesByUserId(id) < 0) { // 用户角色可能为空
            throw new CustomizeBusinessException(HiMsgCdConstants.DEL_USER_ROLE_FAIL, "删除用户角色失败");
        } else {
            if (userDao.delUser(id) < 1) {
                throw new CustomizeBusinessException(HiMsgCdConstants.DEL_USER_FAIL, "删除用户失败");
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {CustomizeBusinessException.class})
    public boolean updUser(UserRequestDto request) {
        // 更新用户基本信息前先更新用户角色
        List<RolePo> rolePoList = roleDao.qryRolesByUserId(request.getId());
        List<RolePo> roles = request.getRoles();
        List<Long> addList = new ArrayList<>(); // 新增角色列表
        List<Long> delList = new ArrayList<>(); // 删除角色列表
        addList = roles.stream().map(RolePo :: getId).collect(Collectors.toList());
        List<Long> roleList = roles.stream().map(RolePo :: getId).collect(Collectors.toList());
        for (RolePo rolePo : rolePoList) {
            Long id = rolePo.getId();
            if (addList.contains(id)) {
                addList.remove(id);
            }
            if (!roleList.contains(id)) {
                delList.add(id);
            }
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", request.getId());
        paramMap.put("roles", delList);
        if (!CollectionUtils.isEmpty(delList)) {
            if (userDao.delUserRoles(paramMap) < 1) {
                throw new CustomizeBusinessException(HiMsgCdConstants.DEL_USER_ROLE_FAIL, "删除用户角色失败");
            }
        }
        if (!CollectionUtils.isEmpty(addList)) {
            paramMap.put("roles", addList);
            if (userDao.addUserRoles(paramMap) < 1) {
                throw new CustomizeBusinessException(HiMsgCdConstants.ADD_USER_ROLE_FAIL, "添加用户角色失败");
            }
        }

        // 更新用户基本信息
        UserPo userPo = BeanUtil.trans2Entity(request, UserPo.class);
        userPo.setDeptId(userPo.getDept().getId());
        userPo.setJobId(userPo.getJob().getId());
        userPo.setUpdTm(new Date().getTime());
        if (userDao.updUser(userPo) < 1) {
            throw new CustomizeBusinessException(HiMsgCdConstants.UPD_USER_FAIL, "更新用户失败");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {CustomizeBusinessException.class})
    public boolean updAvatar(MultipartFile multipartFile) {
        if(!FileUtil.isAllowedSize(maxSize, multipartFile.getSize())) {
            throw new BadRequestException("文件超出规定大小");
        }
        File file = FileUtil.upload(multipartFile, avatarPath + File.separator);
        if (!StringUtils.isEmpty(file)) {
            // 更新用户头像
            UserPo userPo = userDao.qryUserById(SecurityUtil.getUserId());
            String oldAvatarPath = userPo.getAvatar(); // 保留旧的头像路径
            String[] array = avatarPath.split("/");
            String relativePath = "/" + array[array.length-2] + "/" + array[array.length-1] + "/" + file.getName();
            userPo.setAvatar(relativePath);
            if (userDao.updUser(userPo) < 1) {
                throw new CustomizeBusinessException(HiMsgCdConstants.UPD_USER_AVATAR_FAIL, "更新用户头像失败");
            }

            // 更新头像成功后删除原有头像
            if (!StringUtils.isEmpty(oldAvatarPath)) {
                String[] arrays = oldAvatarPath.split("/");
                StringBuilder sb = new StringBuilder();
                for (int i=3; i<arrays.length; i++) {
                    sb.append("/" + arrays[i]);
                }
                String absolutePath = avatarPath + sb.toString();
                FileUtil.del(absolutePath);
            }
        }
        return true;
    }

    @Override
    public boolean updUserBasic(UserRequestDto request) {
        // 更新用户基本信息
        UserPo userPo = BeanUtil.trans2Entity(request, UserPo.class);
        userPo.setUpdTm(new Date().getTime());
        if (userDao.updUserBasic(userPo) < 1) {
            throw new CustomizeBusinessException(HiMsgCdConstants.UPD_USER_FAIL, "更新用户失败");
        }
        return true;
    }
}
