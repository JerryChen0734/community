package com.jerrychen.community.mapper;

import com.jerrychen.community.model.Comment;
import com.jerrychen.community.model.CommentExample;
import com.jerrychen.community.model.Question;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CommentExtMapper {
    int incCommentCount(Comment record);
}