package com.cc.community.service;

import com.cc.community.dto.PaginationDTO;
import com.cc.community.dto.QuestionDTO;
import com.cc.community.exception.CustomizeErrorCode;
import com.cc.community.exception.CustomizeException;
import com.cc.community.mapper.QuestionExtMapper;
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
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;


    public PaginationDTO list(Integer page, Integer size, String searchQuestion) {
        //获得所有问题数
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = null;
        if (searchQuestion.length() == 0) {
            totalCount = (int) questionMapper.countByExample(new QuestionExample());
        } else {
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andTitleLike("%"+searchQuestion+"%");
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
        if (searchQuestion.length() == 0) {
            QuestionExample questionExample = new QuestionExample();
            questionExample.setOrderByClause("gmt_create desc");
            questionList = questionMapper.selectByExampleWithRowbounds(questionExample, new RowBounds(offSet, size));
        }else{
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andTitleLike("%"+searchQuestion+"%");
            questionExample.setOrderByClause("gmt_create desc");
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


    public PaginationDTO listMyQuestions(Long id, Integer page, Integer size) {
        //获得所有问题数
        PaginationDTO paginationDTO = new PaginationDTO();
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(id);
        questionExample.setOrderByClause("gmt_create desc");
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


    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NULL_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void incView(Long id) {
        Question record = new Question();
        record.setId(id);
        record.setViewCount(1);
        questionExtMapper.incView(record);
    }
}
