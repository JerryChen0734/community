package com.jerrychen.dome.controller;

import com.jerrychen.dome.dto.AccessTokenDTO;
import com.jerrychen.dome.dto.GithubUser;
import com.jerrychen.dome.mapper.UserMapper;
import com.jerrychen.dome.model.User;
import com.jerrychen.dome.provider.GithubProvider;
import com.jerrychen.dome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.uri}")
    private String clientUri;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @GetMapping("/callback")
    String callback(@RequestParam(name = "code") String code,
                    @RequestParam(name = "state") String state,
                    HttpServletResponse response
    ) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(clientUri);
        accessTokenDTO.setState(state);

        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if (null != githubUser && null != githubUser.getId()) {
            String token = UUID.randomUUID().toString();
            User user = new User();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(githubUser.getAvatarUrl());
            userService.createOrUpdate(user);

            //登录成功
            response.addCookie(new Cookie("token", token));
            return "redirect:/";


        } else {
            //登录失败，重新登录
            return "redirect:/";


        }
    }

    @GetMapping("/logout")
    String logout(HttpServletRequest request,
    HttpServletResponse response
    ) {
        request.getSession().removeAttribute("user");
        Cookie cookie=new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/";
    }


}
