package com.jerrychen.dome.controller;

import com.jerrychen.dome.dto.PaginationDTO;
import com.jerrychen.dome.mapper.UserMapper;
import com.jerrychen.dome.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    String index(HttpServletRequest request,
                 Model model,
                 @RequestParam(name = "page", defaultValue = "1") Integer page,
                 @RequestParam(name = "size", defaultValue = "5") Integer size

    ) {

        PaginationDTO pagination = questionService.list(page,size);

        model.addAttribute("pagination", pagination);
        return "index";
    }


}

