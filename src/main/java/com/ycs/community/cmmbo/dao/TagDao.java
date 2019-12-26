package com.ycs.community.cmmbo.dao;

import com.ycs.community.cmmbo.domain.po.TagPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TagDao {
    List<TagPo> qryTagList(long pid);
    List<TagPo> qryMostStarTagList();
    List<TagPo> qryTagListByIds(List<String> ids);
}
