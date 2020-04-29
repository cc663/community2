package com.cc.community.service;

import com.cc.community.dto.CommentDTO;
import com.cc.community.dto.PaginationDTO;
import com.cc.community.dto.QuestionDTO;
import com.cc.community.enums.CommentTypeEnum;
import com.cc.community.exception.CustomizeErrorCode;
import com.cc.community.exception.CustomizeException;
import com.cc.community.mapper.CommentMapper;
import com.cc.community.mapper.QuestionExtMapper;
import com.cc.community.mapper.QuestionMapper;
import com.cc.community.mapper.UserMapper;
import com.cc.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: community
 * @description: Comment Service
 * @author: Chao
 * @create: 2020-04-25 17:45
 **/
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Transactional
    public void insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NULL_FOUND);
        }

        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.COM_TYPE_NOT_FOUND);
        }

        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COM_NOT_FOUND);
            }
            commentMapper.insert(comment);
        } else {
            //回复问题
            Question dbQuestion = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (dbQuestion == null) {
                throw new CustomizeException(CustomizeErrorCode.QUES_NOT_FOUND);
            }
            commentMapper.insert(comment);
            //comment count 加一
            dbQuestion.setCommentCount(1);
            questionExtMapper.incComment(dbQuestion);
        }

    }


    public List<CommentDTO> getDTOlistByComments(List<Comment> comments){
        if (comments.size() == 0){
            return new ArrayList<>();
        }

        Set<Long> commentators = comments.stream().map(comment -> comment.getCommenator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);

        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommenator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }

    public PaginationDTO list(Integer page, Integer size, Long id) {
        //获得所有问题数
        PaginationDTO paginationDTO = new PaginationDTO();
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(CommentTypeEnum.QUESTION.getType());
        commentExample.setOrderByClause("gmt_create desc");
        Integer totalCount = (int)commentMapper.countByExample(commentExample);

        //set分页属性
        paginationDTO.setPagination(page, size, totalCount);

        //获得验证后的page
        page = paginationDTO.getPage();

        //传入offset， 即limit的 偏移量
        Integer offSet = size * (page - 1);

        //取得当前页comment
        List<Comment> commentList = commentMapper.selectByExampleWithRowbounds(commentExample, new RowBounds(offSet, size));

        //通过question获得对应user，一起传进questionDTO中
        List<CommentDTO> listIndex = getDTOlistByComments(commentList);

        //传入分页
        paginationDTO.setCommentDTOList(listIndex);

        return paginationDTO;

    }

}
