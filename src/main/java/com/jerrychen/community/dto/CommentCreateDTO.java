package com.jerrychen.community.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CommentCreateDTO {

    private Long parentId;
    private String content;
    private Integer type;
}
