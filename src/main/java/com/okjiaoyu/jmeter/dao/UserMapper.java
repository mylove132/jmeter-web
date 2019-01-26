package com.okjiaoyu.jmeter.dao;

import com.okjiaoyu.jmeter.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM entity_user where name = #{userName}")
    User findByUserName(String userName);

    @Select("SELECT id FROM entity_user where name = #{userName}")
    Integer findUserId(String userName);
}
