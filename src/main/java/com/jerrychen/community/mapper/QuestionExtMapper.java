package com.jerrychen.community.mapper;

import com.jerrychen.community.dto.QuestionQueryDTO;
import com.jerrychen.community.model.Question;
import com.jerrychen.community.model.QuestionExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface QuestionExtMapper {
  int incView(Question record);
  int incCommentCount(Question record);

  Integer countBySearch(QuestionQueryDTO questionQueryDTO);

  List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}