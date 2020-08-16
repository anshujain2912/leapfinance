package com.example.demo.objects;

import java.util.List;
import java.util.Map;

public class ContentResponse extends BaseResponse{

    List<Content> contentMap;

    public List<Content> getContentMap() {
        return contentMap;
    }

    public void setContentMap(List<Content> contentMap) {
        this.contentMap = contentMap;
    }
}
