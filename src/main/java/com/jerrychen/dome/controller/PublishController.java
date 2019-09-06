package com.jerrychen.dome.controller;

import com.jerrychen.dome.mapper.QuestionMapper;
import com.jerrychen.dome.mapper.UserMapper;
import com.jerrychen.dome.model.Question;
import com.jerrychen.dome.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @GetMapping("/publish")
    String publish() {
        return "publish";
    }

    @PostMapping("/publish")
    String doPublish(
            @RequestParam("title") String tietle,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            HttpServletRequest request,
            Model model

    ) {
        model.addAttribute("title",tietle);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        if (null==tietle||""==tietle){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if (null==description||""==description){
            model.addAttribute("error","问题补充不能为空");
            return "publish";
        }
        if (null==tag||""==tag){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }


        User user=null;
        Cookie[] cookies =request.getCookies();
        if (null!=cookies){
            for (Cookie cookie:cookies){
                if (cookie.getName().equals(("token"))){
                    String token=cookie.getValue();
                    user=userMapper.findByToken(token);
                    if (null!=user){
                        request.getSession().setAttribute("user",user);

                    }
                    break;
                }
            }
        }
        if (null==user){
            model.addAttribute("error","用户未登录");
            return "publish";
        }
        Question question =new Question();
        question.setTitle(tietle);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());

        questionMapper.create(question);
        return "redirect:/";

    }
}
