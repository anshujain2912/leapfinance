package com.example.demo.controller;

import com.example.demo.objects.BaseResponse;
import com.example.demo.objects.ContentResponse;
import com.example.demo.service.ContentService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content")
public class ContentController {
    @Autowired
    ContentService contentService;
    @GetMapping("/save")
    @ResponseBody
    public ResponseEntity<BaseResponse> save(@RequestParam String userName, @RequestParam String accessToken, @RequestParam String url){
        BaseResponse response=new BaseResponse();
        try {
            response = contentService.save(userName, accessToken, url);
        }
        catch (Exception e){
            response.setStatusCode(400);
            response.setMessage("Internal Server Error.");
        }
        return new ResponseEntity<BaseResponse>(response, HttpStatus.valueOf(response.getStatusCode()));
    }
    @GetMapping("/getContent")
    @ResponseBody
    public ResponseEntity<ContentResponse> fetchAllUrl(@RequestParam String userName, @RequestParam String accessToken){
        ContentResponse contentResponse = contentService.fetchUserUrls(userName, accessToken);
        return new ResponseEntity<ContentResponse>(contentResponse, HttpStatus.valueOf(contentResponse.getStatusCode()));
    }
}
