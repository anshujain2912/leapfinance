package com.example.demo.service;

import com.example.demo.dao.ContentDao;
import com.example.demo.objects.BaseResponse;
import com.example.demo.objects.ContentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class ContentService {
    @Autowired
    UserService userService;
    @Autowired
    ContentDao contentDao;

    public BaseResponse save(String userName,String accessToken, String url) throws IOException {
        BaseResponse response=new BaseResponse();
        boolean isValid= userService.validateToken(userName,accessToken);
        if(!isValid){
            response.setStatusCode(404);
            response.setMessage("User or token is not valid.");
        }
        else{
            String content=getText(url);
            boolean ifUrlPresent=contentDao.checkIfUrlPresent(url);
            if(ifUrlPresent){
                boolean isUserMappedToUrl=contentDao.checkIfUserMappedToUrl(userName,url);
                if(isUserMappedToUrl) {
                    response.setStatusCode(201);
                    response.setMessage("User is already mapped to Url");
                }
                contentDao.saveUserToUrl(userName,url,content);
                response.setStatusCode(200);
                response.setMessage("Url is saved for the user.");
            }
            else {
                contentDao.saveContent(userName, url, content);
                response.setStatusCode(200);
                response.setMessage("Url is saved for the user.");
            }
        }
        return  response;
    }

    private String getText(String Url) throws IOException {
        URL url = new URL(Url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        return content.toString();
    }

    public ContentResponse fetchUserUrls(String userName, String accessToken){
        ContentResponse response=new ContentResponse();
        boolean isValid= userService.validateToken(userName,accessToken);
        if(!isValid){
            response.setStatusCode(404);
            response.setMessage("User or token is not valid.");
        }
        else{
            response.setContentMap(contentDao.fetchUserUrls(userName));
            response.setStatusCode(200);
        }
        return response;
    }
}
