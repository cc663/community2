package com.cc.community.provider;

import com.alibaba.fastjson.JSON;
import com.cc.community.dto.AccessTokenDTO;
import com.cc.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String access_token = StringUtils.split(string, "&")[0].split("=")[1];
            System.out.println("Response body ========>> "+string);
            System.out.println("Accually token =========>> "+access_token);
            return access_token;
        } catch (IOException e) {
        }
        return null;
    }

        public GithubUser getUser(String accessToken) throws IOException {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.github.com/user?access_token="+ accessToken)
                    .build();
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        }
}
