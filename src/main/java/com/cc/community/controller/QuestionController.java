package com.cc.community.controller;

import com.cc.community.dto.CommentDTO;
import com.cc.community.dto.PaginationDTO;
import com.cc.community.dto.QuestionDTO;
import com.cc.community.service.CommentService;
import com.cc.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id,
                           @RequestParam(name = "page", defaultValue = "1") Integer page, //页码
                           @RequestParam(name = "size", defaultValue = "5") Integer size, //分页数
                           Model model){
        QuestionDTO questionDTO = questionService.getById(id);
        questionService.incView(id);
        PaginationDTO commentsDTO = commentService.list(page, size, id);
        model.addAttribute("question",questionDTO);
        model.addAttribute("commentsDTO",commentsDTO);
        return "question";
    }

}
