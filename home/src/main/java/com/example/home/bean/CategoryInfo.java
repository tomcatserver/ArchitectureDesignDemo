package com.example.home.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 常见症状分类描述信息 (改版)
 * ps: 包含子节点
 * Created by yujin on 2018/9/12
 */
public class CategoryInfo implements Serializable {

    private static final long serialVersionUID = 123456789123123121L;

    /**
     * 跳转类目 (点进去的需要的参数)
     */
    @Expose
    private String content;

    /**
     * 父类Id
     */
    @Expose
    private String fatherId;

    /**
     * 二级类目id
     */
    @Expose
    private String id;

    /**
     * 类目名称
     */
    @Expose
    private String name;

    /**
     * 类目下的子类目信息
     */
    @Expose
    private ArrayList<CategoryInfo> thridCategory;

    @Expose
    private String subtitle;

    @Expose
    private String type;

    @Expose
    private String icon;

    @Expose
    private String hitBiCat;
    ;//1-BI

    @Expose
    private String triggerType;

    public String getContent() {
        return content;
    }

    public String getHitBiCat() {
        return hitBiCat;
    }

    public void setHitBiCat(String hitBiCat) {
        this.hitBiCat = hitBiCat;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<CategoryInfo> getThirdCategory() {
        return thridCategory;
    }

    public void setThridCategory(ArrayList<CategoryInfo> thirdCategory) {
        this.thridCategory = thirdCategory;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }
}
