package com.ycs.community.sysbo.domain.po;

import com.ycs.community.spring.security.utils.SecurityUtil;
import com.ycs.community.sysbo.service.DeptService;
import com.ycs.community.sysbo.service.RoleService;
import com.ycs.community.sysbo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataScope {
    private final String[] scopeType = {"全部","本级","自定义"};
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private DeptService deptService;

    /**
     * 获取部门IDS
     * @return
     */
    public List<Long> getDeptIds() {
        UserPo userPo = userService.qryUserById(SecurityUtil.getUserId());

        // 用于存储部门ID
        List<Long> deptIds = new ArrayList<>();

        // 查询用户角色
        List<RolePo> roles = roleService.qryRolesByUserId();

        for (RolePo role : roles) {
            // 全部的数据权限
            if (scopeType[0].equals(role.getDataScope())) {
                return new ArrayList<>() ;
            }

            // 存储本级的数据权限
            if (scopeType[1].equals(role.getDataScope())) {
                deptIds.add(userPo.getDept().getId());
            }

            // 存储自定义的数据权限
            if (scopeType[2].equals(role.getDataScope())) {
                List<DeptPo> depts = deptService.qryDeptsByRoleId(role.getId());
                for (DeptPo dept : depts) {
                    deptIds.add(dept.getId());
                    deptIds = getDeptFatherIds(dept.getPid(), deptIds);
                    List<DeptPo> children = deptService.qryDeptsByPid(dept.getId());
                    if (!CollectionUtils.isEmpty(children)) {
                        deptIds.addAll(getDeptChildrenIds(children));
                    }
                }
            }
        }
        return deptIds.stream().distinct().collect(Collectors.toList()); // 去重
    }

    /**
     * 递归获取所有父部门ID
     * @param pid
     * @param deptIds
     * @return
     */
    private List<Long> getDeptFatherIds(Long pid, List<Long> deptIds) {
        deptIds.add(pid);
        DeptPo deptPo = deptService.qryDeptById(pid);
        if (deptPo.getPid() != 0) {
            getDeptFatherIds(deptPo.getPid(), deptIds);
        }
        return deptIds;
    }

    /**
     * 获取子部门IDS
     * @param deptList
     * @return
     */
    public List<Long> getDeptChildrenIds(List<DeptPo> deptList) {
        List<Long> result = new ArrayList<>();
        deptList.forEach(dept -> {
            List<DeptPo> depts = deptService.qryDeptsByPid(dept.getId());
            if(!CollectionUtils.isEmpty(depts)){
                result.addAll(getDeptChildrenIds(depts));
            }
            result.add(dept.getId());
        });
        return result;
    }

    /**
     * 获取角色IDS
     * @return
     */
    public List<Long> getRoleIds() {
        List<Long> roleIds = new ArrayList<>();
        // 查询用户角色
        List<RolePo> roles = roleService.qryRolesByUserId();
        for (RolePo role : roles) {
            // 全部的数据权限
            if (scopeType[0].equals(role.getDataScope())) {
                return new ArrayList<>();
            }

            // 存储本级的数据权限
            if (scopeType[1].equals(role.getDataScope())) {
                roleIds.add(role.getId());
            }

            // 存储自定义的数据权限
            if (scopeType[2].equals(role.getDataScope())) {
                roleIds.add(role.getId());
            }
        }
        return roleIds;
    }
}