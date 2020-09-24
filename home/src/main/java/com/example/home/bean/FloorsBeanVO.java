package com.example.home.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yujin on 2019/1/17
 */
public class FloorsBeanVO implements Serializable {

    @Expose
    private int id;
    @Expose
    private String name;
    @Expose
    private int type; // 楼层类型
    @Expose
    private String bgImage;
    @Expose
    private List<ResourceLocationsBeanVO> resourceLocations;

    /**
     * 圆角类型 null,all,bottom,top
     */
    private String bgType;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<ResourceLocationsBeanVO> getResourceLocations() {
        return resourceLocations;
    }

    public String getBgImage() {
        return bgImage;
    }

    public void setResourceLocations(List<ResourceLocationsBeanVO> resourceLocations) {
        this.resourceLocations = resourceLocations;
    }

    public boolean resourceLocationsIsNull() {
        if (resourceLocations == null || resourceLocations.size() == 0) {
            return true;
        }
        return false;
    }

    public String getBgType() {
        return bgType;
    }

    public void setBgType(String bgType) {
        this.bgType = bgType;
    }

    /**
     * 基础楼层type
     */
    public static final int TYPE_REC = 2001;  // 动态button 楼层

    public static final int TYPE_TOP_BUTTON = 200007;  // 动态button 楼层

    public static final int TYPE_MULTI_BANNER = 200008;  // banner 楼层

    public static final int TYPE_DRAG_SAFETY_CERTIFICATION = 200009;  // 品宣药监认证 楼层

    public static final int TYPE_OLD_CUSTOMER_NOTICE = 200011;   // 老用户通知楼层

    public static final int TYPE_NEW_CUSTOMER_NOTICE = 200010;  // 新用户通知楼层

    public static final int TYPE_SYSTEM_NOTICE = 200012;   // 系统公告通知楼层

    public static final int TYPE_TOP_TWO_BOTTOM_FOUR = 200013;  // 上2下4 楼层

    public static final int TYPE_ONE_TWO = 200014;  // 1x2 楼层

    public static final int TYPE_SYMPTOM_FIND_MEDICINE = 200015;  // 对症找药

    public static final int TYPE_HEALTH_US_SHARE = 200016;  // 健康优享

    public static final int TYPE_SINGLE_BANNER = 200017; // 单帧广告楼层

    public static final int TYPE_MAPS_FOOOR = 200018; // Mps楼层

    public static final int TYPE_FOR_YOUR_SELECTION = 200019; // 为你精选楼层

    public static final int TYPE_TOP_ONE_BOTTOM_TWO = 1007;  // 上一下二楼层

    public static final int TYPE_YIZHEN = 200024;//一诊

    public static final int TYPE_QRQM_TOP_BUTTON = 200031; // 千人千面button
    public static final int GOLD_LEVEL_10 = 200040; //  千人千面button * 10

    public static final int TYPE_GRID_LEVEL_5 = 200052; // gird布局，一行5个

    public static final int TYPE_QRQM_GOODS_ENTRANCE = 200030; // 千人千面商品入口

    public static final int TYPE_QRQM_HEALTH_SAID = 200034; // 千人千面健康说

    public static final int TYPE_QRQM_GOODS_RECOMMEND_BANNER = 200029; // 商品推荐广告位

    public static final int TYPE_FOUR_ICONS = 200051; //1X4 高90

    /**
     * 首页活动type
     */
    public static final int TYPE_ACTIVITY_HOME_BOTTOM = 200020; // 底部活动

    public static final int TYPE_HOME_CHANGE_SEARCH_KEYWORD = 200054; // 首页关键词

    public static final int TYPE_USER_WELFARE_CENTER = 200038; // 个人中心福利中心

    public static final int TYPE_HEADER_BACKGROUND = 200042; //头部背景


    public static final int TYPE_PULL_REFRESH_BACKGROUND = 200043; // 下拉刷新背景

    public static final int TYPE_ONE_COLUMN = 200047;//中通单帧

    public static final int TYPE_GAP = 200048;

    public static final int TYPE_DETAIL_SECRET = 200022; // 商详隐私图

    public static final int TYPE_CARE_BAO_BANNER = 200049; // 商详关爱宝活动

    public static final int TYPE_JYZZ_ENTER = 200050; // 经营资质

    public static final int TYPE_NEW_USER = 200053; // 新人活动

    public static final int TYPE_INQUIRY = 200055; // 问诊楼层数据
}
