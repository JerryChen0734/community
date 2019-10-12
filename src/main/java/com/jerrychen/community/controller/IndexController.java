package com.jerrychen.community.controller;

import com.jerrychen.community.service.QuestionService;
import com.jerrychen.community.dto.PaginationDTO;
import com.jerrychen.community.mapper.UserMapper;
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
                 @RequestParam(name = "size", defaultValue = "5") Integer size,
                 @RequestParam(name = "search", required = false) String search

    ) {


            PaginationDTO pagination = questionService.list(search,page,size);
            model.addAttribute("pagination", pagination);

        PaginationDTO hotQuestionPagination = questionService.hotList(10);
        model.addAttribute("search", search);
        model.addAttribute("hotQuestionPagination", hotQuestionPagination);
        return "index";
    }


}

