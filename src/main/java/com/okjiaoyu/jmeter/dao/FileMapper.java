package com.okjiaoyu.jmeter.dao;

import com.okjiaoyu.jmeter.entity.FileEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: liuzhanhui
 * @Decription:
 * @Date: Created in 2019-01-22:10:00
 * Modify date: 2019-01-22:10:00
 */
@Mapper
public interface FileMapper {

    @Insert("INSERT INTO entity_file(name,uid,file,createDate) VALUES(#{fileEntity.fileName},#{fileEntity.uid},#{fileEntity.file},#{fileEntity.createDate})")
    int addOrUpdateFile(FileEntity fileEntity);
}
