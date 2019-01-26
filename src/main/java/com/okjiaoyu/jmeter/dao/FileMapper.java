package com.okjiaoyu.jmeter.dao;

import com.okjiaoyu.jmeter.entity.FileEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * @Author: liuzhanhui
 * @Decription:
 * @Date: Created in 2019-01-22:10:00
 * Modify date: 2019-01-22:10:00
 */
@Mapper
public interface FileMapper {

    @Insert({"INSERT INTO entity_file(name,uid,createTime,execTime) VALUES(#{fileEntity.name},#{fileEntity.uid},#{fileEntity.createTime},#{fileEntity.execTime});"})
    int addScript(@Param("fileEntity") FileEntity fileEntity);

    @Select({"SELECT id FROM entity_file WHERE name=#{fileName} AND createTime=#{createTime}"})
    Integer getFileId(String fileName, Date createTime);

    @Insert({"INSERT INTO entity_file(execTime) VALUES(#{execTime}) WHERE id = #{fileId}"})
    int addExecTime(Integer fileId, Date execTime);

    @Select("SELECT name FROM entity_file where uid=#{uid}")
    List<String> queryFileNames(Integer uid);
}
