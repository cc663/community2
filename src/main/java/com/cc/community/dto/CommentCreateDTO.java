package com.cc.community.dto;

import lombok.Data;

/**
 * @program: community
 * @description: comment DTO from created
 * @author: Chao
 * @create: 2020-04-25 14:19
 **/
@Data
public class CommentCreateDTO {
    private Long parentId;
    private String content;
    private Integer type;
}
