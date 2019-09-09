package com.jerrychen.dome.service;

import com.jerrychen.dome.dto.PaginationDTO;
import com.jerrychen.dome.dto.QuestionDTO;
import com.jerrychen.dome.mapper.QuestionMapper;
import com.jerrychen.dome.mapper.UserMapper;
import com.jerrychen.dome.model.Question;
import com.jerrychen.dome.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    //查询文章表，然后找出对应的创建者并存进list返回
    public PaginationDTO list(Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();

        Integer totalCount = questionMapper.count();

        paginationDTO.setPagination(totalCount,page,size);


        Integer offset = size * (page - 1);

        List<Question> questions = questionMapper.list(offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findByCreator(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }



        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;

    }
}
