package com.jerrychen.dome.provider;

import com.alibaba.fastjson.JSON;
import com.jerrychen.dome.dto.AccessTokenDTO;
import com.jerrychen.dome.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {
  public   String getAccessToken(AccessTokenDTO accessTokenDTO){
      MediaType mediaType= MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(mediaType,JSON.toJSONString(accessTokenDTO));
            Request request = new Request.Builder()
                    .url("https://github.com/login/oauth/access_token")
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                    String S=response.body().string();
                    String token=S.split("&")[0].split("=")[1];
                   // System.out.println(token);
                    return token;

                } catch (IOException e) {

                }
             return null;
            }

        public     GithubUser getUser(String accessToken){
                OkHttpClient client = new OkHttpClient();


                    Request request = new Request.Builder()
                            .url("https://api.github.com/user?access_token"+accessToken)
                            .build();

                    try {
                        Response response = client.newCall(request).execute();
                        String S=response.body().string();
                        GithubUser githubUser=  JSON.parseObject(S,GithubUser.class);
                        return  githubUser;
                    } catch (IOException e) {
                    }
                    return null;

                }
            }

