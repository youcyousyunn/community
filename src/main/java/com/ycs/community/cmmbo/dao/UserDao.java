package com.ycs.community.cmmbo.dao;

import com.ycs.community.cmmbo.domain.po.TopicPo;
import com.ycs.community.cmmbo.domain.po.UserPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserDao {
    UserPo qryUserByAccountId(Long accountId);
    int addUser(UserPo userPo);
    int updUser(UserPo userPo);
    UserPo qryUserInfoByName(String name);
}
