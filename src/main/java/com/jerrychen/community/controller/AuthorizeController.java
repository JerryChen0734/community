package com.jerrychen.community.controller;

import com.jerrychen.community.dto.AccessTokenDTO;
import com.jerrychen.community.dto.GithubUser;
import com.jerrychen.community.exception.CustomizeErrorCode;
import com.jerrychen.community.exception.CustomizeException;
import com.jerrychen.community.mapper.UserMapper;
import com.jerrychen.community.model.User;
import com.jerrychen.community.provider.GithubProvider;
import com.jerrychen.community.service.UserService;
import io.swagger.annotations.Example;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@Log4j2
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
                    HttpServletResponse response,
                    Model model
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
            log.error("callback get github error,{}",githubUser);
            //登录失败，重新登录
            return "redirect:/";


        }
    }

    @GetMapping("/logout")
    String logout(HttpServletRequest request,
                  HttpServletResponse response
    ) {
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        if (request.getSession().getAttribute("user") == "" || request.getSession().getAttribute("user") == null) {

            return "redirect:/";
        } else {
            return this.logout(request, response);
        }

    }

    @PostMapping("/signup")
    String signup(@RequestParam(name = "name") String name,
                  @RequestParam(name = "password") String password,
                  @RequestParam(name = "repassword") String repassword,
                  HttpServletResponse response){

        if (!password.equals(repassword)){
            throw new CustomizeException(CustomizeErrorCode.PASSWORD_WRONG);
        }

        String token = UUID.randomUUID().toString();
        User user = new User();
        user.setToken(token);
        user.setName(name);
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtModified(user.getGmtCreate());
        user.setAvatarUrl("https://avatars2.githubusercontent.com/u/54530844?v=4");
        user.setPassword(password);
        userService.createOrUpdate(user);

        //登录成功
        response.addCookie(new Cookie("token", token));
        return "redirect:/";

    }

    @PostMapping("/signin")
    String signin(@RequestParam(name = "name") String name,
                  @RequestParam(name = "password") String password,
                  HttpServletResponse response){

        String token=userService.signin(name, password);
        if (token!=null||token.length()>=0){
            //登录成功
            response.addCookie(new Cookie("token", token));
        }
        return "redirect:/";

    }


}
