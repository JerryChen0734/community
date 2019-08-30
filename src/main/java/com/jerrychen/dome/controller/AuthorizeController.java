package com.jerrychen.dome.controller;

import com.jerrychen.dome.dto.AccessTokenDTO;
import com.jerrychen.dome.dto.GithubUser;
import com.jerrychen.dome.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @GetMapping("/callback")
    String callback(@RequestParam(name="code")String code,
                    @RequestParam(name="state")String state
                    ){
           AccessTokenDTO accessTokenDTO= new AccessTokenDTO();
           accessTokenDTO.setClient_id("5e6caff25e933fb47159");
           accessTokenDTO.setClient_secret("870faf1c724188d9a0e031f90ab3295772890bff");
           accessTokenDTO.setCode(code);
           accessTokenDTO.setRedirect_uri("http://localhost:8080/callback");
           accessTokenDTO.setState(state);

       String accessToken= githubProvider.getAccessToken(accessTokenDTO);
        GithubUser user=githubProvider.getUser(accessToken);
        System.out.println(user.getName());
    return "index";
    }


}
