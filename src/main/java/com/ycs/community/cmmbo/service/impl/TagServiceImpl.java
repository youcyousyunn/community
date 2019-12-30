package com.ycs.community.cmmbo.service.impl;

import com.ycs.community.basebo.constants.HiMsgCdConstants;
import com.ycs.community.basebo.utils.BeanUtil;
import com.ycs.community.cmmbo.dao.TagDao;
import com.ycs.community.cmmbo.domain.dto.TagRequestDto;
import com.ycs.community.cmmbo.domain.dto.TagResponseDto;
import com.ycs.community.cmmbo.domain.po.TagPo;
import com.ycs.community.cmmbo.service.TagService;
import com.ycs.community.spring.exception.BadRequestException;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.sysbo.domain.po.MenuPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagDao tagDao;

    @Override
    public TagResponseDto qryTagTree(TagRequestDto request) {
        Map<String, Object> paramMap = new HashMap<>();
        if (!StringUtils.isEmpty(request.getStartTime())) {
            paramMap.put("startTime", request.getStartTime().getTime());
        }
        if (!StringUtils.isEmpty(request.getEndTime())) {
            paramMap.put("endTime", request.getEndTime().getTime());
        }
        paramMap.put("name", request.getName());
        List<TagPo> tagPoList = tagDao.qryTagTree(paramMap);

        // 构建标签树
        List<TagPo> tagTree = new LinkedList<>();
        tagPoList.forEach(tagPo -> {
            if (0 == tagPo.getPid()) {
                tagTree.add(tagPo);
            }
            for (TagPo tag : tagPoList) {
                if (tagPo.getId() == tag.getPid()) {
                    if (CollectionUtils.isEmpty(tagPo.getChildren())) {
                        tagPo.setChildren(new ArrayList<>());
                    }
                    tagPo.getChildren().add(tag);
                }
            }
        });

        TagResponseDto response = new TagResponseDto();
        if (!CollectionUtils.isEmpty(tagTree)) {
            response.setData(tagTree);
        }
        return response;
    }

    @Override
    public List<Map<String, Object>> qryTagTabTree(List<TagPo> tags) {
        List<Map<String, Object>> list = new LinkedList<>();
        tags.forEach(tag -> {
            List<TagPo> tagPoList = tagDao.qryTagListByPid(tag.getId());
            Map<String,Object> map = new HashMap<>();
            map.put("id", tag.getId());
            map.put("label", tag.getName());
            if (!CollectionUtils.isEmpty(tagPoList)) {
                map.put("children", qryTagTabTree(tagPoList));
            }
            list.add(map);
        });
        return list;
    }

    @Override
    public List<TagPo> qryTagListByPid(long pid) {
        List<TagPo> data = tagDao.qryTagListByPid(pid);
        return data;
    }

    @Override
    public TagResponseDto qryMostStarTagList() {
        TagResponseDto response = new TagResponseDto();
        List<TagPo> data = tagDao.qryMostStarTagList();
        if (!CollectionUtils.isEmpty(data)) {
            response.setData(data);
        }
        return response;
    }

    @Override
    @Transactional(rollbackFor = CustomizeBusinessException.class)
    public boolean addTag(TagRequestDto request) {
        TagPo tagPo = BeanUtil.trans2Entity(request, TagPo.class);
        tagPo.setCreTm(new Date().getTime());
        if (tagDao.addTag(tagPo) < 1) {
            throw new CustomizeBusinessException(HiMsgCdConstants.ADD_TAG_FAIL, "添加标签失败");
        }
        return true;
    }

    @Override
    public boolean delTag(Long id) {
        // 删除标签前判断是否存在子标签
        List<TagPo> children = tagDao.qryTagListByPid(id);
        if (!CollectionUtils.isEmpty(children)) {
            throw new CustomizeBusinessException(HiMsgCdConstants.HAS_CHILDREN_CAN_NOT_DEL_TAG, "存在子标签, 不能删除");
        }
        if (tagDao.delTag(id) < 1) {
            throw new CustomizeBusinessException(HiMsgCdConstants.DEL_TAG_FAIL, "删除标签失败");
        }
        return true;
    }

    @Override
    public boolean updTag(TagRequestDto request) {
        if(request.getId().equals(request.getPid())) {
            throw new BadRequestException("上级不能为自己");
        }
        TagPo tagPo = BeanUtil.trans2Entity(request, TagPo.class);
        tagPo.setUpdTm(new Date().getTime());
        if (tagDao.updTag(tagPo) < 1) {
            throw new CustomizeBusinessException(HiMsgCdConstants.UPD_TAG_FAIL, "更新标签失败");
        }
        return true;
    }
}