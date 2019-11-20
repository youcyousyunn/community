package com.ycs.community.cmmbo.dao;

import com.ycs.community.cmmbo.domain.po.UserPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDao {
    UserPo qryUserById(Long id);
    int addUser(UserPo userPo);
    int updUser(UserPo userPo);
    UserPo qryUserInfoByName(String name);
}
