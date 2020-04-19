package com.cc.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {

    private List<QuestionDTO> questionDTOList = new ArrayList<>(); //当前页面显示的所有分页对象
    private Integer page; //当前页码
    private List<Integer> pages = new ArrayList<>(); //当前展示的页码集合
    private Integer size; //所有页码数
    private boolean showFirstFlag;  // <<
    private boolean showLastFlag;  //  >>
    private boolean showPreviousFlag; // <
    private boolean showNextFlag;    // >

    public void setPagination(Integer page, Integer size, Integer totalCount) {

        Integer totalPages;
        if (totalCount !=0 && totalCount >= size) {
            if (totalCount % size == 0) {
                totalPages = totalCount / size;
            } else {
                totalPages = totalCount / size + 1;
            }
        }else{
            totalPages = 1;
        }

        //传入总的page数
        this.size = totalPages;

        //规定输入 page 范围
        if (page < 1){
            page = 1;
        }

        if (page > totalPages){
            page = totalPages;
        }

        //传入当前page
        this.page = page;

        if (page == 1){
            showPreviousFlag = false;
        }else {
            showPreviousFlag = true;
        }

        if (page == totalPages){
            showNextFlag = false;
        }else {
            showNextFlag = true;
        }

        //前三列
        if (page == 1 && totalPages >= 4){
            pages.add(1);
            pages.add(2);
            pages.add(3);
            pages.add(4);
        }

        if (page == 2 && totalPages >= 5){
            pages.add(1);
            pages.add(2);
            pages.add(3);
            pages.add(4);
            pages.add(5);
        }

        if (page == 3 && totalPages >= 6){
            pages.add(1);
            pages.add(2);
            pages.add(3);
            pages.add(4);
            pages.add(5);
            pages.add(6);
        }

        //when totalPages 大于等于 7页时，点击 4 和 max - 3 之间的任意页数都显示七个page，其中点击页居中
        if (page >= 4 && page <= totalPages - 3 && totalPages >= 7 ) {
            for (int i = page - 3; i <= page; i++) {
                pages.add(i);
            }

            for (int i = page + 1; i <= page + 3; i++) {
                pages.add(i);
            }
        }

        //后三列
        if (totalPages >= 7 && page == totalPages){
            pages.add(totalPages - 3);
            pages.add(totalPages - 2);
            pages.add(totalPages - 1);
            pages.add(totalPages);
        }

        if (totalPages >= 7 && page == totalPages - 1) {
            pages.add(totalPages - 4);
            pages.add(totalPages - 3);
            pages.add(totalPages - 2);
            pages.add(totalPages - 1);
            pages.add(totalPages);
        }

        if (totalPages >= 7 && page == totalPages - 2) {
            pages.add(totalPages - 5);
            pages.add(totalPages - 4);
            pages.add(totalPages - 3);
            pages.add(totalPages - 2);
            pages.add(totalPages - 1);
            pages.add(totalPages);
        }

        if (pages.isEmpty()) {
            for (int i = 1; i <= totalPages; i++) {
                pages.add(i);
            }
        }

        if (pages.contains(1)){
            showFirstFlag = false;
        }else {
            showFirstFlag = true;
        }

        if (pages.contains(totalPages)){
            showLastFlag = false;
        }else {
            showLastFlag = true;
        }

    }

}
