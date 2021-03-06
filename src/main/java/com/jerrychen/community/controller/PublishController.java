package com.jerrychen.community.controller;

import com.jerrychen.community.dto.QuestionDTO;
import com.jerrychen.community.dto.ResultDTO;
import com.jerrychen.community.service.PublishService;
import com.jerrychen.community.service.QuestionService;
import com.jerrychen.community.model.Question;
import com.jerrychen.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {


    @Autowired
    private QuestionService questionService;



    @GetMapping("/publish/{id}")
    String edit(@PathVariable(name = "id") Long id,
                Model model) {
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("id", question.getId());
        return "publish";

    }

    @GetMapping("/publish")
    String publish() {
        return "publish";
    }


    @PostMapping("/publish")
    String doPublish(
            @RequestParam("title") String tietle,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            @RequestParam("id") Long id,
            HttpServletRequest request,
            Model model

    ) {
        model.addAttribute("title", tietle);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        if (null == tietle || "" == tietle) {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (null == description || "" == description) {
            model.addAttribute("error", "问题补充不能为空");
            return "publish";
        }
        if (null == tag || "" == tag) {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }


        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            return "redirect:/";

        }
        if (null == user) {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }
        Question question = new Question();
        question.setTitle(tietle);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setId(id);
        questionService.creatrOrUpdate(question);
        return "redirect:/";

    }



}
