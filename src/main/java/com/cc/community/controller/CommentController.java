package com.cc.community.controller;

import com.cc.community.dto.CommentCreateDTO;
import com.cc.community.dto.CommentDTO;
import com.cc.community.dto.ResultDTO;
import com.cc.community.enums.CommentTypeEnum;
import com.cc.community.exception.CustomizeErrorCode;
import com.cc.community.model.Comment;
import com.cc.community.model.User;
import com.cc.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @program: community
 * @description: Comment Controller
 * @author: Chao
 * @create: 2020-04-25 12:36
 **/
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request,
                       Model model){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }

        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }

        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setCommenator(user.getId());
        comment.setLikeCount(0l);
        commentService.insert(comment);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> getComments(@PathVariable(name = "id") Long id
    ) {
        List<CommentDTO> commentDTOList = commentService.getDTOlistById(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOList);
    }


}
