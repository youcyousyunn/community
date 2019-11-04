package com.ycs.community.coobo.dao;

import com.ycs.community.coobo.domain.po.AttachPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface AttachDao {
    int qryAttachCount(Map<String, Object> paramMap);
    List<AttachPo> qryAttachPage(Map<String, Object> paramMap);
    int addAttach(AttachPo attachPo);
    AttachPo qryAttachById(Long id);
    int updAttach(AttachPo attachPo);
    int delAttachById(Long id);
    List<AttachPo> qryAttach(Map<String, Object> paramMap);
}
