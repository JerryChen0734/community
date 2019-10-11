package com.jerrychen.community.dto;

import com.jerrychen.community.model.User;
import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    private Long notifier;
    private String notifierName;
    private  Long outerid;
    private String outerTitle;
    private String typeName;
    private Integer type;

}
