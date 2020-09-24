package com.example.home.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

/**
 * 为你精选 数据结果集
 */
public class NameToProductsTemplate implements Serializable {

    @Expose
    private String tagName;

    @Expose
    private int tagId;

    @Expose
    private List<ItemContentVO> itemsToTemplate;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public List<ItemContentVO> getItemsToTemplate() {
        return itemsToTemplate;
    }

    public void setItemsToTemplate(List<ItemContentVO> itemsToTemplate) {
        this.itemsToTemplate = itemsToTemplate;
    }
}
