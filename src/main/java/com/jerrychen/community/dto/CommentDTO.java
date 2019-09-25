package com.jerrychen.community.dto;

import com.jerrychen.community.model.User;
import lombok.Data;

@Data
public class CommentDTO {

    private Long id;


    private Integer type;

    private Long parentId;


    private Long commentator;

    private Long gmtCreate;


    private Long gmtModified;

    private Long likeCount;


    private String content;

    private User user;

}
