package com.cc.community.service;

import com.cc.community.mapper.UserMapper;
import com.cc.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;


    public void createOrUpdate(User user) {
        User DBUser = userMapper.findByAccountId(user.getAccountId());
        if (DBUser == null){
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else{
            DBUser.setToken(user.getToken());
            DBUser.setAvatarUrl(user.getAvatarUrl());
            DBUser.setGmtCreate(System.currentTimeMillis());
            DBUser.setGmtModified(user.getGmtCreate());
            userMapper.updateByAccountId(DBUser);
        }

    }
}
