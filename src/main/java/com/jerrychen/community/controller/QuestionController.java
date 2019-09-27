package com.jerrychen.community.controller;

import com.jerrychen.community.dto.CommentDTO;
import com.jerrychen.community.dto.QuestionDTO;
import com.jerrychen.community.enums.CommentTypeEnum;
import com.jerrychen.community.service.CommentService;
import com.jerrychen.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @Autowired
    private CommentService commentService;
    @GetMapping("/question/{id}")
    String question(@PathVariable(name = "id") Long id,
                    Model model) {

        QuestionDTO questionDTO = questionService.getById(id);

        List<CommentDTO> comments = commentService.listByTaegetId(id, CommentTypeEnum.QUESTION);
        questionService.incView(id);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);

        return "question";
    }
}
