package com.jerrychen.community.controller;


import com.jerrychen.community.dto.CommentDTO;
import com.jerrychen.community.dto.ResultDTO;
import com.jerrychen.community.exception.CustomizeErrorCode;
import com.jerrychen.community.mapper.CommentMapper;
import com.jerrychen.community.model.Comment;
import com.jerrychen.community.model.User;
import com.jerrychen.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by codedrinker on 2019/5/30.
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request) {
       User user= (User) request.getSession().getAttribute("user");
       if(user==null){
           return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);

       }
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        commentService.insert(comment);
        return ResultDTO.successOf();
    }
}