package com.jerrychen.community.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "回复实体")
public class CommentDTO {
    @ApiModelProperty("父回复ID")
    private Long parentId;
    @ApiModelProperty("内容")
    private  String content;
    @ApiModelProperty("回复层级类型")
    private  Integer type;
}
