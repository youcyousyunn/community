package com.ycs.community.sysbo.dao;

import com.ycs.community.sysbo.domain.po.DictPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface DictDao {
    int qryDictCount(Map<String, Object> paramMap);
    List<DictPo> qryDictPage(Map<String, Object> paramMap);
    List<DictPo> qryDictDetailsByDictId(Long dictId);
    int qryDictDetailCount(Map<String, Object> paramMap);
    List<DictPo> qryDictDetailPage(Map<String, Object> paramMap);
    List<DictPo> qryDictDetailsByName(String name);
    int delDictDetailsByDictId(Long dictId);
    int addDict(DictPo dictPo);
    int delDictById(Long id);
    int updDict(DictPo dictPo);
    int addDictDetail(DictPo dictPo);
    int delDictDetailById(Long id);
    int updDictDetail(DictPo dictPo);
}
