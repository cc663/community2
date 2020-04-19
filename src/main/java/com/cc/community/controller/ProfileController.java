package com.cc.community.controller;

import com.cc.community.dto.PaginationDTO;
import com.cc.community.mapper.UserMapper;
import com.cc.community.model.User;
import com.cc.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable("action") String action,
                          HttpServletRequest request,
                          Model model,
                          @RequestParam(name = "page", defaultValue = "1") Integer page, //页码
                          @RequestParam(name = "size", defaultValue = "2") Integer size //分页数
                          ) {

        Cookie[] cookies = request.getCookies();
        User user = null;
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    user = userMapper.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }

        if (user == null) {
            return "redirect:/";
        }

        if (action.equals("questions")) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
        } else if (action.equals("replies")) {
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "我的回复");
        }

        PaginationDTO myQuestionsPagination = questionService.listMyQuestions(user.getId(), page, size);

        model.addAttribute("myQuestionsPagination", myQuestionsPagination);

        return "profile";
    }

}
