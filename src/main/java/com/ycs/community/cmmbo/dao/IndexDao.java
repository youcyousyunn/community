package com.ycs.community.cmmbo.dao;

import com.ycs.community.cmmbo.domain.po.TopicPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository  // 也可以使用@Component，效果都是一样的，只是为了声明为bean
public interface IndexDao {
    List<TopicPo> qryTopicList();

}
