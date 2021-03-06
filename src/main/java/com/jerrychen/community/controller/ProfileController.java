package com.jerrychen.community.controller;

import com.jerrychen.community.dto.NotificationDTO;
import com.jerrychen.community.dto.PaginationDTO;
import com.jerrychen.community.model.Notification;
import com.jerrychen.community.service.NotificationService;
import com.jerrychen.community.service.QuestionService;
import com.jerrychen.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ProfileController {


    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    String profile(HttpServletRequest request,
                   @PathVariable(name = "action") String action,
                   Model model,
                   @RequestParam(name = "page", defaultValue = "1") Integer page,
                   @RequestParam(name = "size", defaultValue = "5") Integer size) {



       User user= (User) request.getSession().getAttribute("user");

        if (user==null){
            return "redirect:/";

        }


        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
            PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
            model.addAttribute("pagination", paginationDTO);
        } else if ("replies".equals(action)) {

            PaginationDTO paginationDTO = notificationService.list(user.getId(), page, size);
            Long unreadCount=notificationService.unreadCount(user.getId());
            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("section", "replies");
            model.addAttribute("unreadCount", unreadCount);
            model.addAttribute("sectionName", "我的回复");
        } else {
            return "redirect:";
        }

        return "profile";

    }
}

