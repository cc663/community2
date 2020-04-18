package com.cc.community.controller;

import com.cc.community.dto.PaginationDTO;
import com.cc.community.dto.QuestionDTO;
import com.cc.community.mapper.QuestionMapper;
import com.cc.community.mapper.UserMapper;
import com.cc.community.model.Question;
import com.cc.community.model.User;
import com.cc.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        @RequestParam(name = "searchQuestion", defaultValue = "") String searchQuestion,
                        @RequestParam(name = "page", defaultValue = "1") Integer page, //页码
                        @RequestParam(name = "size", defaultValue = "5") Integer size, //分页数
                        Model model) {
        model.addAttribute("searchQuestion",searchQuestion);

        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }

        //分页显示
        PaginationDTO questionIndex = questionService.list(page, size, searchQuestion);

        model.addAttribute("questions",questionIndex.getQuestionDTOList());
        model.addAttribute("pages",questionIndex.getPages());
        model.addAttribute("page",questionIndex.getPage());
        model.addAttribute("size",questionIndex.getSize());
        model.addAttribute("isShowFirstFlag",questionIndex.isShowFirstFlag());
        model.addAttribute("isShowLastFlag",questionIndex.isShowLastFlag());
        model.addAttribute("isShowNextFlag",questionIndex.isShowNextFlag());
        model.addAttribute("isShowPreviousFlag",questionIndex.isShowPreviousFlag());
        return "index";
    }
}