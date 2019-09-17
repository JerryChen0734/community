package com.jerrychen.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {
    private List<QuestionDTO> questions;
    private Boolean showPrevious;
    private Boolean showFirstPage;
    private Boolean showNext;
    private Boolean showEndPage;

    private Integer page;
    private List<Integer> pages = new ArrayList<>();
    private Integer totalPage = 0;

    /*
    1.接受页面数据后判断是否打开开关
    2.将当前分页后的页面展示存进pages给前端调用
     */

    public void setPagination(Integer totalPage, Integer page) {

        this.totalPage = totalPage;

        this.page = page;
        pages.add(page);

        for (int i = 1; i < 4; i++) {
            if (page - i > 0) {
                pages.add(0, page - i);

            }
            if (page + i <= totalPage) {
                pages.add(page + i);

            }
        }


        if (page == 1) {
            showPrevious = false;
        } else {
            showPrevious = true;
        }

        if (page == totalPage) {
            showNext = false;
        } else {
            showNext = true;
        }

        if (pages.contains(1)) {
            showFirstPage = false;

        } else {
            showFirstPage = true;
        }

        if (pages.contains(totalPage)) {
            showEndPage = false;

        } else {
            showEndPage = true;
        }
    }
}
