package com.cc.community.mapper;

import com.cc.community.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
     @Insert("insert into user (name, account_id, token, gmt_create, gmt_modified, avatar_url) values (#{name}, #{accountId},  #{token}, #{gmtCreate}, #{gmtModified}, #{avatarUrl})")
     void insert(User user);

     @Select("select * from user where token = #{token} and rownum = 1")
     User findByToken(@Param("token") String token);

     @Select("select * from user where id = #{id}")
     User findById(@Param("id") Integer id);

     @Select("Select * from user where account_id = #{account_id} and rownum = 1")
     User findByAccountId(@Param("account_id") String account_id);

     @Update("update user set token = #{token}, gmt_create = #{gmtCreate}, gmt_modified = #{gmtModified}, avatar_url = #{avatarUrl} where account_id = #{accountId} ")
     void updateByAccountId(User dbUser);
}
