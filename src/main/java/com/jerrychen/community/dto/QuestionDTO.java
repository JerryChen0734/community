package com.jerrychen.community.dto;

import com.jerrychen.community.model.User;
import lombok.Data;

@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer viewCount=0;
    private Integer commentCount=0;
    private Integer likeCount=0;
    private User user;

}
