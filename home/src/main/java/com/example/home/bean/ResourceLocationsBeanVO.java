package com.example.home.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yujin on 2019/1/17
 */
public class ResourceLocationsBeanVO implements Serializable {

    @Expose
    private int id;
    @Expose
    private String name;
    @Expose
    private List<FramesBeanVO> frames;

    @Expose
    private int tag; // 商品标记：1：我购买的；2：收藏过的；3：浏览过的

    private int position = -1; // 统计位置的作用，表示第几个元素

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FramesBeanVO> getFrames() {
        return frames;
    }

    public void setFrames(List<FramesBeanVO> frames) {
        this.frames = frames;
    }

    public int getPosition() {
        return position;
    }

    public ResourceLocationsBeanVO setPosition(int position) {
        this.position = position;
        return this;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public boolean framesIsNull() {
        if (frames == null || frames.size() == 0 ) {
            return true;
        }
        return false;
    }
}
