package com.example.home.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by yujin on 2019/1/17
 */
public class FramesBeanVO implements Serializable {

    @Expose
    private int id;
    @Expose
    private HomeChangeContentBeanVO content;
    @Expose
    private String name;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HomeChangeContentBeanVO getContent() {
        return content;
    }

    public void setContent(HomeChangeContentBeanVO content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean contentIsNull() {
        if (content == null) {
            return true;
        }
        return false;
    }


}
