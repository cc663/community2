package com.cc.community.service;

import com.cc.community.dto.PaginationDTO;
import com.cc.community.dto.QuestionDTO;
import com.cc.community.mapper.QuestionMapper;
import com.cc.community.mapper.UserMapper;
import com.cc.community.model.Question;
import com.cc.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;


    public PaginationDTO list(Integer page, Integer size, String question) {
        //获得所有问题数
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = null;
        if (question == "") {
            totalCount = questionMapper.count();
        } else {
            totalCount = questionMapper.countByTitle(question);
        }

        //set分页属性
        paginationDTO.setPagination(page, size, totalCount);

        //获得验证后的page
        page = paginationDTO.getPage();

        //传入offset， 即limit的 偏移量
        Integer offSet = size * (page - 1);

        //验证question
        List<Question> questionList = new ArrayList<>();
        if (question == "") {
            questionList = questionMapper.list(offSet, size);
        }else{
            questionList = questionMapper.listByTitle(offSet, size, question);
        }

        //通过question获得对应user，一起传进questionDTO中
        List<QuestionDTO> listIndex = new ArrayList<>();
        for (Question ques : questionList) {
            User user = userMapper.findById(ques.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(ques, questionDTO);
            questionDTO.setUser(user);
            listIndex.add(questionDTO);
        }

        //传入分页
        paginationDTO.setQuestionDTOList(listIndex);

        return paginationDTO;
    }


}
