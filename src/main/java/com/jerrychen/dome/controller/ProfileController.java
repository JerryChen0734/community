package com.jerrychen.dome.controller;

import com.jerrychen.dome.dto.PaginationDTO;
import com.jerrychen.dome.mapper.UserMapper;
import com.jerrychen.dome.model.User;
import com.jerrychen.dome.service.QuestionService;
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
    String profile(HttpServletRequest request,
                   @PathVariable(name = "action") String action,
                   Model model,
                   @RequestParam(name = "page", defaultValue = "1") Integer page,
                   @RequestParam(name = "size", defaultValue = "5") Integer size) {
        User user=null;
        Cookie[] cookies = request.getCookies();
        if (null != cookies && 0 != cookies.length) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(("token"))) {
                    String token = cookie.getValue();
                    user = userMapper.findByToken(token);
                    if (null != user) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        if (user==null){
            return "redirect:/";

        }


        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
        } else if ("replies".equals(action)) {
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "我的回复");
        } else {
            return "redirect:";
        }

        PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);


        model.addAttribute("pagination", paginationDTO);
        return "profile";

    }
}

