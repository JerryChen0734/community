package com.jerrychen.community.service;

import com.jerrychen.community.exception.CustomizeErrorCode;
import com.jerrychen.community.exception.CustomizeException;
import com.jerrychen.community.model.UserExample;
import com.jerrychen.community.provider.GithubProvider;
import com.jerrychen.community.mapper.UserMapper;
import com.jerrychen.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private UserMapper userMapper;

    public  String signin(String name, String password) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNameEqualTo(name);
        List<User> users = userMapper.selectByExample(userExample);
        for (User user : users) {
            if (user.getPassword().equals(password)){
                return user.getToken();
            }else {
                throw new CustomizeException(CustomizeErrorCode.PASSWORD_WRONG);
            }
        }
        return null;
    }


    public void createOrUpdate(User user) {
        //AccountId为空则新建账户，不为空则判断是否在数据库中有重复
        //有重复则更新，没有则新建
        if (user.getAccountId()!=null && user.getAccountId()!=""){
            UserExample userExample = new UserExample();
            userExample.createCriteria()
                    .andAccountIdEqualTo(user.getAccountId());
            List<User> users = userMapper.selectByExample(userExample);
            if (users.size()== 0) {
                user.setGmtCreate(System.currentTimeMillis());
                user.setGmtModified(user.getGmtCreate());
                userMapper.insert(user);
            }else {
                User dbUser = users.get(0);
                User updateUser = new User();

                updateUser.setGmtModified(user.getGmtCreate());
                updateUser.setAvatarUrl(user.getAvatarUrl());
                updateUser.setName(user.getName());
                updateUser.setToken(user.getToken());
                UserExample example = new UserExample();
                example.createCriteria()
                        .andIdEqualTo(dbUser.getId());
                userMapper.updateByExampleSelective(updateUser, example);




            }
        }else {
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }


    }
}
