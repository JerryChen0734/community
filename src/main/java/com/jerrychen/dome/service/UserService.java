package com.jerrychen.dome.service;

import com.jerrychen.dome.mapper.UserMapper;
import com.jerrychen.dome.model.User;
import com.jerrychen.dome.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private UserMapper userMapper;


    public void createOrUpdate(User user) {
       User  dbUser= userMapper.findByAccountId(user.getAccountId()) ;
        if (dbUser== null) {
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else {
            dbUser.setGmtModified(user.getGmtCreate());
            dbUser.setAvatarUrl(user.getAvatarUrl());
            dbUser.setName(user.getName());
            dbUser.setToken(user.getToken());
            userMapper.update(dbUser);


        }
    }
}
