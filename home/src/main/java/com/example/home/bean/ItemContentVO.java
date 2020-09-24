package com.example.home.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by yujin on 2019/4/17 0017.
 */
public class ItemContentVO implements Serializable {

    @Expose
    private String productNo;
    @Expose
    private String gift; // 显示文描：此处为免费礼品
    @Expose
    private double originalPrice; // 显示图片
    @Expose
    private String mainimg1; // 跳转链接
    @Expose
    private int itemId; // 商品id
    @Expose
    private String productName; // 显示图片
    @Expose
    private double recommendPrice;
    @Expose
    private String itemName;
    @Expose
    private int isRecommend; // 为1的情况需要打推荐的标识

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getGift() {
        return gift;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getMainimg1() {
        return mainimg1;
    }

    public void setMainimg1(String mainimg1) {
        this.mainimg1 = mainimg1;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getRecommendPrice() {
        return recommendPrice;
    }

    public void setRecommendPrice(double recommendPrice) {
        this.recommendPrice = recommendPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(int isRecommend) {
        this.isRecommend = isRecommend;
    }
}
