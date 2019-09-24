package com.jerrychen.community.service;

import com.jerrychen.community.enums.CommentTypeEnum;
import com.jerrychen.community.exception.CustomizeErrorCode;
import com.jerrychen.community.exception.CustomizeException;
import com.jerrychen.community.mapper.CommentMapper;
import com.jerrychen.community.mapper.QuestionExtMapper;
import com.jerrychen.community.mapper.QuestionMapper;
import com.jerrychen.community.model.Comment;
import com.jerrychen.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;

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
}
