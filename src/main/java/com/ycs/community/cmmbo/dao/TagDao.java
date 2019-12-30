package com.ycs.community.cmmbo.dao;

import com.ycs.community.cmmbo.domain.po.TagPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TagDao {
    List<TagPo> qryTagTree(Map<String, Object> paramMap);
    List<TagPo> qryTagListByPid(long pid);
    List<TagPo> qryMostStarTagList();
    List<TagPo> qryTagListByIds(List<String> ids);
    int addTag(TagPo tagPo);
    int delTag(Long id);
    int updTag(TagPo tagPo);
}
