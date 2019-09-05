package com.jerrychen.dome.controller;

import com.jerrychen.dome.mapper.UserMapper;
import com.jerrychen.dome.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/")
    String index(HttpServletRequest request){
       Cookie[] cookies =request.getCookies();
       for (Cookie cookie:cookies){
           if (cookie.getName().equals(("token"))){
               String token=cookie.getValue();
               User user=userMapper.findByToken(token);
               if (null!=user){
                   request.getSession().setAttribute("user",user);
               }
               break;
           }

       }

        return "index";
    }





    }

