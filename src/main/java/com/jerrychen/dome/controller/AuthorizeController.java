package com.jerrychen.dome.controller;

import com.jerrychen.dome.dto.AccessTokenDTO;
import com.jerrychen.dome.dto.GithubUser;
import com.jerrychen.dome.mapper.UserMapper;
import com.jerrychen.dome.model.User;
import com.jerrychen.dome.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private  String clientId;

    @Value("${github.client.uri}")
    private  String clientUri;

    @Value("${github.client.secret}")
    private  String clientSecret;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    String callback(@RequestParam(name="code")String code,
                    @RequestParam(name="state")String state,
                    HttpServletRequest request
                    ){
           AccessTokenDTO accessTokenDTO= new AccessTokenDTO();
           accessTokenDTO.setClient_id(clientId);
           accessTokenDTO.setClient_secret(clientSecret);
           accessTokenDTO.setCode(code);
           accessTokenDTO.setRedirect_uri(clientUri);
           accessTokenDTO.setState(state);

       String accessToken= githubProvider.getAccessToken(accessTokenDTO);
        System.out.println(accessToken);
        GithubUser githubUser=githubProvider.getUser(accessToken);
        if (null!=githubUser){
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            //登录成功
            request.getSession().setAttribute("user",githubUser);
            return "redirect:/";



        }else {
            //登录失败，重新登录
            return "redirect:/";


        }
    }


}
