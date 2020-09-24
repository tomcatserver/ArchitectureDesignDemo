package com.example.home.bean;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * ABTest 返回的数据模型
 */
public class HomeChangeVO {

    @Expose
    private int status;  // 1.成功 -1.没有数据返回 -2.pageCode校验失败
    @Expose
    private List<FloorsBeanVO> floors;
    @Expose
    private FloorsBeanVO bottomActivityButton; // 底部天窗数据
    @Expose
    private FloatCMSVO floatCMS;
    @Expose
    private List<FloorsBeanVO> homeActivity; // 首页活动
    @Expose
    private int total;  // 数据总数量
    @Expose
    private int pageCount; // 分页总数
    @Expose
    private int currentPageNo; // 当前第几页

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<FloorsBeanVO> getFloors() {
        if (floors == null) {
            floors = new ArrayList<>();
        }
        return floors;
    }

    public void setFloors(List<FloorsBeanVO> floors) {
        this.floors = floors;
    }

    public FloatCMSVO getFloatCMS() {
        return floatCMS;
    }

    public void setFloatCMS(FloatCMSVO floatCMS) {
        this.floatCMS = floatCMS;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public FloorsBeanVO getBottomActivityButton() {
        return bottomActivityButton;
    }

    public void setBottomActivityButton(FloorsBeanVO bottomActivityButton) {
        this.bottomActivityButton = bottomActivityButton;
    }

    public List<FloorsBeanVO> getHomeActivity() {
        return homeActivity;
    }

    public void setHomeActivity(List<FloorsBeanVO> homeActivity) {
        this.homeActivity = homeActivity;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getCurrentPageNo() {
        return currentPageNo;
    }

    public void setCurrentPageNo(int currentPageNo) {
        this.currentPageNo = currentPageNo;
    }
}
