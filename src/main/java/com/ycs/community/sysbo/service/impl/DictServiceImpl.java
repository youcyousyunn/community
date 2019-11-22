package com.ycs.community.sysbo.service.impl;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.basebo.utils.BeanUtil;
import com.ycs.community.basebo.utils.PageUtil;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.sysbo.dao.DictDao;
import com.ycs.community.sysbo.domain.dto.*;
import com.ycs.community.sysbo.domain.po.DictPo;
import com.ycs.community.sysbo.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DictServiceImpl implements DictService {
    @Autowired
    private DictDao dictDao;

    @Override
    public QryDictPageResponseDto qryDictPage(QryDictPageRequestDto request) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", request.getName());
        // 查询总条数
        int totalCount = dictDao.qryDictCount(paramMap);
        // 计算分页信息
        PageUtil.calculatePageInfo(totalCount, request.getCurrentPage(), request.getPageSize());
        // 分页查询
        paramMap.put("startRow", PageUtil.getStartRow());
        paramMap.put("offset", PageUtil.getPageSize());
        List<DictPo> data = dictDao.qryDictPage(paramMap);
        // 查询对应字典详情
        data.forEach(dictPo -> {
            List<DictPo> dictDetails = dictDao.qryDictDetailsByDictId(dictPo.getId());
            dictPo.setDictDetails(dictDetails);
        });
        // 组装分页信息
        QryDictPageResponseDto response = new QryDictPageResponseDto();
        if (!CollectionUtils.isEmpty(data)) {
            response.setData(data);
            response.setTotal(totalCount);
            return response;
        }
        return response;
    }

    @Override
    public QryDictDetailPageResponseDto qryDictDetailPage(QryDictDetailPageRequestDto request) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("dictId", request.getDictId());
        paramMap.put("label", request.getLabel());
        // 查询总条数
        int totalCount = dictDao.qryDictDetailCount(paramMap);
        // 计算分页信息
        PageUtil.calculatePageInfo(totalCount, request.getCurrentPage(), request.getPageSize());
        // 分页查询
        paramMap.put("startRow", PageUtil.getStartRow());
        paramMap.put("offset", PageUtil.getPageSize());
        List<DictPo> data = dictDao.qryDictDetailPage(paramMap);
        // 组装分页信息
        QryDictDetailPageResponseDto response = new QryDictDetailPageResponseDto();
        if (!CollectionUtils.isEmpty(data)) {
            response.setData(data);
            response.setTotal(totalCount);
            return response;
        }
        return response;
    }

    @Override
    public DictDetailResponseDto qryDictDetailsByName(DictRequestDto request) {
        List<DictPo> data = dictDao.qryDictDetailsByName(request.getName());
        DictDetailResponseDto response = new DictDetailResponseDto();
        if(!CollectionUtils.isEmpty(data)) {
            response.setData(data);
        }
        return response;
    }

    @Override
    @Transactional(rollbackFor = {CustomizeBusinessException.class})
    public boolean addDict(DictRequestDto request) {
        DictPo dictPo = BeanUtil.trans2Entity(request, DictPo.class);
        dictPo.setCreTm(new Date().getTime());
        if (dictDao.addDict(dictPo) > 0) {
            return true;
        } else {
            throw new CustomizeBusinessException(HiMsgCdConstants.ADD_DICT_FAIL, "添加字典失败");
        }
    }

    @Override
    @Transactional(rollbackFor = {CustomizeBusinessException.class})
    public boolean delDict(Long id) {
        // 删除字典前先删除字典详情
        if (dictDao.delDictDetailsByDictId(id) < 0) {
            throw new CustomizeBusinessException(HiMsgCdConstants.DEL_DICT_DETAIL_FAIL, "删除字典详情失败");
        } else {
            if (dictDao.delDictById(id) < 1) {
                throw new CustomizeBusinessException(HiMsgCdConstants.DEL_DICT_FAIL, "删除字典失败");
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {CustomizeBusinessException.class})
    public boolean updDict(DictRequestDto request) {
        DictPo dictPo = BeanUtil.trans2Entity(request, DictPo.class);
        dictPo.setUpdTm(new Date().getTime());
        if (dictDao.updDict(dictPo) > 0) {
            return true;
        } else {
            throw new CustomizeBusinessException(HiMsgCdConstants.UPD_DICT_FAIL, "修改字典失败");
        }
    }

    @Override
    @Transactional(rollbackFor = {CustomizeBusinessException.class})
    public boolean addDictDetail(DictDetailRequestDto request) {
        DictPo dictPo = BeanUtil.trans2Entity(request, DictPo.class);
        dictPo.setCreTm(new Date().getTime());
        if (dictDao.addDictDetail(dictPo) > 0) {
            return true;
        } else {
            throw new CustomizeBusinessException(HiMsgCdConstants.ADD_DICT_DETAIL_FAIL, "添加字典详情失败");
        }
    }

    @Override
    @Transactional(rollbackFor = {CustomizeBusinessException.class})
    public boolean delDictDetail(Long id) {
        if (dictDao.delDictDetailById(id) < 1) {
            throw new CustomizeBusinessException(HiMsgCdConstants.DEL_DICT_DETAIL_FAIL, "删除字典详情失败");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {CustomizeBusinessException.class})
    public boolean updDictDetail(DictDetailRequestDto request) {
        DictPo dictPo = BeanUtil.trans2Entity(request, DictPo.class);
        dictPo.setUpdTm(new Date().getTime());
        if (dictDao.updDictDetail(dictPo) > 0) {
            return true;
        } else {
            throw new CustomizeBusinessException(HiMsgCdConstants.UPD_DICT_DETAIL_FAIL, "修改字典详情失败");
        }
    }
}