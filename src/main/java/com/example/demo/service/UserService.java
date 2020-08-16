package com.example.demo.service;

import com.example.demo.dao.UserDao;
import com.example.demo.objects.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    public LoginResponse validateUser(String userName,String password){
        LoginResponse response=new LoginResponse();
        boolean valid=userDao.validateUser(userName,password);
        String accessToken="";
        if(valid){
            accessToken=generateAccessToken(userName);
            userDao.saveAccessToken(userName,accessToken);
            response.setStatusCode(200);
            response.setAccessToken(accessToken);
            response.setUserName(userName);
        }else {
            response.setStatusCode(404);
            response.setMessage("Invalid Credentials");
        }
        return response;
    }

    private String generateAccessToken(String userName) {
        return UUID.randomUUID().toString();
    }

    public boolean validateToken(String userName, String accessToken){
        return userDao.validateToken(userName,accessToken);
    }
}
