package com.cc.community.mapper;

import com.cc.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into question (title, description, gmt_create, gmt_modified, creator, tag) values (#{title}, #{description}, #{gmtCreate}, #{gmtModified}, #{creator}, #{tag})")
    void insertQuestion(Question question);

    @Select("select * from question limit #{offSet}, #{size}")
    List<Question> list(Integer offSet, Integer size);

    @Select("select count(*) from question")
    Integer count();

    @Select("select * from question where title like '%'||#{title}||'%' limit #{offSet}, #{size}")
    List<Question> listByTitle(Integer offSet, Integer size, @Param("title") String title);

    @Select("select count(*) from question where title like '%'||#{title}||'%' ")
    Integer countByTitle(@Param("title") String title);

    @Select("select count(*) from question where creator = #{creator}")
    Integer countById(@Param("creator") Integer creator);

    @Select("select * from question where creator = #{creator} limit #{offSet}, #{size}")
    List<Question> listById(Integer offSet, Integer size, @Param("creator") Integer creator);

    @Select("select * from question where id = #{id}")
    Question getById(@Param("id") Integer id);

    @Update("update question set tag = #{tag}, description = #{description}, title = #{title}, gmt_modified = #{gmtModified} where id = #{id}")
    void update(Question question);
}
