package com.cc.community.controller;

import com.cc.community.mapper.QuestionMapper;
import com.cc.community.model.Question;
import com.cc.community.model.QuestionExample;
import com.cc.community.model.User;
import com.cc.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class PublishController {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }

    @GetMapping("/publish/{id}")
    public String publish(@PathVariable("id") Integer id,
                          Model model){
        Question question = questionMapper.selectByPrimaryKey(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("id", id);
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(@RequestParam("title") String title,
                            @RequestParam("description") String description,
                            @RequestParam("tag") String tag,
                            @RequestParam("id") Integer id,
                            HttpServletRequest request,
                            Model model) {
        //输入内容回显
        model.addAttribute("title",title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);

        if (title == null || title == ""){
            model.addAttribute("error","标题不能为空！");
            return "publish";
        }
        if (description == null || description == ""){
            model.addAttribute("error"," 问题描述不能为空！");
            return "publish";
        }
        if (tag == null || tag == ""){
            model.addAttribute("error","标签不能为空！");
            return "publish";
        }

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }

        Question question = new Question();
        question.setDescription(description);
        question.setTag(tag);
        question.setTitle(title);

        if (id == null){
            question.setCreator(user.getId());
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setCommentCount(0);
            question.setViewCount(0);
            questionMapper.insert(question);
        }else{
            question.setGmtModified(System.currentTimeMillis());
            question.setId(id);
            questionMapper.updateByPrimaryKeySelective(question);
        }

        return "redirect:/profile/questions";
    }
}

