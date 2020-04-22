package com.cc.community.service;

import com.cc.community.dto.PaginationDTO;
import com.cc.community.dto.QuestionDTO;
import com.cc.community.mapper.QuestionMapper;
import com.cc.community.mapper.UserMapper;
import com.cc.community.model.Question;
import com.cc.community.model.QuestionExample;
import com.cc.community.model.User;
import org.apache.ibatis.session.RowBounds;
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
        if (question.length() == 0) {
            totalCount = (int) questionMapper.countByExample(new QuestionExample());
        } else {
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andTitleLike(question);
            totalCount = (int) questionMapper.countByExample(questionExample);
        }

        //set分页属性
        paginationDTO.setPagination(page, size, totalCount);

        //获得验证后的page
        page = paginationDTO.getPage();

        //传入offset， 即limit的 偏移量
        Integer offSet = size * (page - 1);

        //验证question
        List<Question> questionList = new ArrayList<>();
        if (question.length() == 0) {
            questionList = questionMapper.selectByExampleWithRowbounds(new QuestionExample(),new RowBounds(offSet, size));
        }else{
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andTitleEqualTo(question);
            questionList = questionMapper.selectByExampleWithRowbounds(questionExample,new RowBounds(offSet, size));
        }

        //通过question获得对应user，一起传进questionDTO中
        List<QuestionDTO> listIndex = new ArrayList<>();
        for (Question ques : questionList) {
            User user = userMapper.selectByPrimaryKey(ques.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(ques, questionDTO);
            questionDTO.setUser(user);
            listIndex.add(questionDTO);
        }

        //传入分页
        paginationDTO.setQuestionDTOList(listIndex);

        return paginationDTO;
    }


    public PaginationDTO listMyQuestions(Integer id, Integer page, Integer size) {
        //获得所有问题数
        PaginationDTO paginationDTO = new PaginationDTO();
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(id);
        Integer totalCount = (int)questionMapper.countByExample(questionExample);

        //set分页属性
        paginationDTO.setPagination(page, size, totalCount);

        //获得验证后的page
        page = paginationDTO.getPage();

        //传入offset， 即limit的 偏移量
        Integer offSet = size * (page - 1);

        //验证question
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(questionExample, new RowBounds(offSet, size));

        //通过question获得对应user，一起传进questionDTO中
        List<QuestionDTO> listIndex = new ArrayList<>();
        for (Question ques : questionList) {
            User user = userMapper.selectByPrimaryKey(ques.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(ques, questionDTO);
            questionDTO.setUser(user);
            listIndex.add(questionDTO);
        }

        //传入分页
        paginationDTO.setQuestionDTOList(listIndex);

        return paginationDTO;
    }


    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

}
