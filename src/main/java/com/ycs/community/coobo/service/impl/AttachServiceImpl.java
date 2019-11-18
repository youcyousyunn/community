package com.ycs.community.coobo.service.impl;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.basebo.utils.PageUtil;
import com.ycs.community.coobo.dao.AttachDao;
import com.ycs.community.coobo.domain.dto.AttachRequestDto;
import com.ycs.community.coobo.domain.dto.QryAttachPageRequestDto;
import com.ycs.community.coobo.domain.dto.QryAttachPageResponseDto;
import com.ycs.community.coobo.domain.po.AttachPo;
import com.ycs.community.coobo.service.AttachService;
import com.ycs.community.coobo.utils.FileUtil;
import com.ycs.community.spring.exception.BadRequestException;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.spring.security.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AttachServiceImpl implements AttachService {
    @Autowired
    private AttachDao attachDao;
    @Value("${file.path}")
    private String uploadPath;
    @Value("${file.max-size}")
    private long maxSize;

    @Override
    @Transactional(rollbackFor = {BadRequestException.class, CustomizeBusinessException.class})
    public boolean upload(MultipartFile multipartFile, String name) {
        if(!FileUtil.isAllowedSize(maxSize, multipartFile.getSize())) {
            throw new BadRequestException("文件超出规定大小");
        }
        String suffix = FileUtil.getExtensionName(multipartFile.getOriginalFilename());
        String type = FileUtil.getFileType(suffix);
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        File file = FileUtil.upload(multipartFile, uploadPath + dateStr + File.separator + type +  File.separator);
        if (!StringUtils.isEmpty(file)) {
            name = StringUtils.isEmpty(name) ? FileUtil.getFileNameNoEx(multipartFile.getOriginalFilename()) : name;
            AttachPo attachPo = new AttachPo();
            attachPo.setName(name);
            attachPo.setRealNm(file.getName());
            attachPo.setPath(file.getPath());
            attachPo.setSuffix(suffix);
            attachPo.setType(type);
            attachPo.setSize(FileUtil.getSize(multipartFile.getSize()));
            attachPo.setOperator(SecurityUtil.getUserName());
            attachPo.setCreTm(new Date().getTime());
            if (1 != attachDao.addAttach(attachPo)) {
                FileUtil.deleteFile(file); // 删除已上传文件
                throw new CustomizeBusinessException(HiMsgCdConstants.UPLOAD_ATTACH_FAIL, "上传附件失败");
            }
        } else {
            throw new BadRequestException("上传文件失败");
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = CustomizeBusinessException.class)
    public boolean updAttach(AttachRequestDto request) {
        AttachPo attachPo = attachDao.qryAttachById(request.getId());
        if (!StringUtils.isEmpty(attachPo)) {
            attachPo.setName(request.getName());
            if (1 == attachDao.updAttach(attachPo)) {
                return true;
            } else {
                throw new CustomizeBusinessException(HiMsgCdConstants.UPD_ATTACH_FAIL, "更新附件失败");
            }
        } else {
            throw new CustomizeBusinessException(HiMsgCdConstants.ATTACH_NOT_EXIST, "附件不存在");
        }
    }

    @Override
    @Transactional(rollbackFor = {BadRequestException.class, CustomizeBusinessException.class})
    public boolean delAttach(Long id) {
        AttachPo attachPo = attachDao.qryAttachById(id);
        if (!StringUtils.isEmpty(attachPo)) {
            boolean result = FileUtil.del(attachPo.getPath());
            if (result) {
                if (1 == attachDao.delAttachById(attachPo.getId())) {
                    return true;
                } else {
                    throw new CustomizeBusinessException(HiMsgCdConstants.DEL_ATTACH_FAIL, "删除附件失败");
                }
            } else {
                throw new BadRequestException("删除文件失败");
            }
        } else {
            throw new CustomizeBusinessException(HiMsgCdConstants.ATTACH_NOT_EXIST, "附件不存在");
        }
    }

    @Override
    public boolean downAttach(AttachRequestDto request, HttpServletResponse response) {
        Map<String, Object> paramMap = new HashMap<>();
        if (!StringUtils.isEmpty(request.getStartTime())) {
            paramMap.put("startTime", request.getStartTime().getTime());
        }
        if (!StringUtils.isEmpty(request.getEndTime())) {
            paramMap.put("endTime", request.getEndTime().getTime());
        }
        paramMap.put("name", request.getName());
        List<AttachPo> attachPoList = attachDao.qryAttach(paramMap);
        List<Map<String, Object>> list = new ArrayList<>();
        for (AttachPo attachPo : attachPoList) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("文件名", attachPo.getRealNm());
            map.put("备注名", attachPo.getName());
            map.put("文件类型", attachPo.getType());
            map.put("文件大小", attachPo.getSize());
            map.put("操作人", attachPo.getOperator());
            map.put("创建日期", new Date(attachPo.getCreTm()));
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
        return true;
    }

    @Override
    @Transactional(rollbackFor = {BadRequestException.class, CustomizeBusinessException.class})
    public boolean delAllAttach(Long[] ids) {
        int result = 0;
        for (Long id : ids) { // 循环遍历删除
            AttachPo attachPo = attachDao.qryAttachById(id);
            if (!StringUtils.isEmpty(attachPo)) {
                boolean isDeleted = FileUtil.del(attachPo.getPath());
                if (isDeleted) {
                    if (1 == attachDao.delAttachById(attachPo.getId())) {
                        result++;
                    } else {
                        throw new CustomizeBusinessException(HiMsgCdConstants.DEL_ATTACH_FAIL, "删除附件失败");
                    }
                } else {
                    throw new BadRequestException("删除文件失败");
                }
            } else {
                throw new CustomizeBusinessException(HiMsgCdConstants.ATTACH_NOT_EXIST, "附件不存在");
            }
        }
        if (result == ids.length) {
            return true;
        }
        return false;
    }

    @Override
    public QryAttachPageResponseDto qryAttachPage(QryAttachPageRequestDto request) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", request.getName());
        if (!StringUtils.isEmpty(request.getStartTime())) {
            paramMap.put("startTime", request.getStartTime().getTime());
        }
        if (!StringUtils.isEmpty(request.getEndTime())) {
            paramMap.put("endTime", request.getEndTime().getTime());
        }
        // 查询总条数
        int totalCount = attachDao.qryAttachCount(paramMap);
        // 计算分页信息
        PageUtil.calculatePageInfo(totalCount, request.getCurrentPage(), request.getPageSize());
        // 分页查询
        paramMap.put("startRow", PageUtil.getStartRow());
        paramMap.put("offset", PageUtil.getPageSize());
        List<AttachPo> data = attachDao.qryAttachPage(paramMap);
        // 组装分页信息
        QryAttachPageResponseDto response = new QryAttachPageResponseDto();
        if (!CollectionUtils.isEmpty(data)) {
            response.setData(data);
            response.setTotal(totalCount);
            return response;
        }
        return response;
    }
}
