package com.cc.community.dto;

import com.cc.community.model.User;
import lombok.Data;

/**
 * @program: community
 * @description: comment DTO from DB
 * @author: Chao
 * @create: 2020-04-28 09:29
 **/
@Data
public class CommentDTO {

    private Long id;
    private Long parentId;
    private Integer type;
    private Long commenator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
    private Long countOfSecondComments;
    private User user;
}
