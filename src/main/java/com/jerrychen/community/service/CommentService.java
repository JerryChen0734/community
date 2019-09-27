package com.jerrychen.community.service;

import com.jerrychen.community.dto.CommentDTO;
import com.jerrychen.community.enums.CommentTypeEnum;
import com.jerrychen.community.exception.CustomizeErrorCode;
import com.jerrychen.community.exception.CustomizeException;
import com.jerrychen.community.mapper.*;
import com.jerrychen.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Transactional
    public void insert(Comment comment) {
        //comment target wrong
        if ((comment.getParentId() == null || comment.getParentId() == 0) || (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType()))) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_WRONG);

        }

        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //Reply to comment
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }

            commentMapper.insert(comment);

            //增加评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);
        } else if (comment.getType() == CommentTypeEnum.QUESTION.getType()) {
            //Reply to question
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
        }
    }
    /*
    如果单纯的查询完回复和对应的user非常耗时，所以为了简化时间复杂度，我将回复的ID取出来，通过一一对应的方法来实现。
    1.取出这篇文章所有的回复
    2.取出所有回复人的ID，并转换成list
    3.根据list中的ID查询所有的user，并转换成map
    4.把文章与user一一对应
     */

    public List<CommentDTO> listByTaegetId(Long id, CommentTypeEnum type) {
        CommentExample example = new CommentExample();
        example.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        example.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(example);
        if (comments.size() == 0) {
            return new ArrayList<>();
        }

        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);

        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }
}
