package com.okjiaoyu.jmeter.dao;

import com.okjiaoyu.jmeter.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM entity_user where name = #{userName}")
    User findByUserName(String userName);
}
