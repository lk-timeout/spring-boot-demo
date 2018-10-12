package com.timeout.mysql;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.timeout.prjo.User;

@Mapper
public interface UserMapper {

	@Select("SELECT * FROM user WHERE NAME = #{name}")
	User findByName(@Param("name") String name);

	@Insert("INSERT INTO user(NAME, AGE) VALUES(#{name}, #{age})")
	int insert(@Param("name") String name, @Param("age") Integer age);

}
