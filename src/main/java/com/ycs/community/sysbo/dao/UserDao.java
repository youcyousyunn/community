package com.ycs.community.sysbo.dao;

import com.ycs.community.sysbo.domain.po.UserPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface UserDao {
    UserPo qryUserById(Long id);
    int addUser(UserPo userPo);
    int updUser(UserPo userPo);
    UserPo qryUserInfoByName(String name);
    int qryUserCount(Map<String, Object> paramMap);
    List<UserPo> qryUserPage(Map<String, Object> paramMap);
}
