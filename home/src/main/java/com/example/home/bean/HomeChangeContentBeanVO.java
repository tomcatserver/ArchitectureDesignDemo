package com.example.home.bean;

import android.text.TextUtils;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yujin on 2019/1/17
 */
@Keep
public class HomeChangeContentBeanVO implements Serializable {

    @Expose
    private String content;
    @Expose
    private int id;
    @Expose
    private String title; // 显示文描：此处为免费礼品
    @Expose
    private String jumpLoginPage; // 是否需要登录，1登录，0不需要登录
    @Expose
    private String triggerValue; // 跳转链接
    @Expose
    private String pic; // 显示图片
    @Expose
    private String unselectedPicture; // 功能非选中图片
    @Expose
    private String selectedPicture; // 功能选中图片
    @Expose
    private String unselectedPermanentPic;
    @Expose
    private String permanentPic;
    @Expose
    private int triggerType;
    @Expose
    private int showType;// 底部tabbar，中间按钮 1.资源位 2.功能位
    @Expose
    private int bgType;
    //首页二期改版 上二拖四楼层新增字段
    @Expose
    private String pic1; //背景图
    @Expose
    private String pic2;//小icon
    @Expose
    private String subtitle;//上二小标题
    @Expose
    private String contentType;
    @Expose
    private int appUserCenterAdSwitch;
    @Expose
    private int appOrderDetailSwitch;
    @Expose
    private String lookProductNum; //查看多少商品触发

    @Expose
    private String productDetailTriggerShakeTime; //商详触发时间

    @Expose
    private String searchTriggerShakeTime; //搜索列表触发时间

    @Expose
    private String shakeTime; //	抖动时间

    @Expose
    private String showShakeTime; //展示气泡时间


    @Expose
    private List<ItemContentVO> itemList;
    @Expose
    private List<CategoryInfo> categoryinfo; // 对症找药
    @Expose
    private List<NameToProductsTemplate> nameToProducts; // 为你精选
    @Expose
    private long startTime;
    @Expose
    private long endTime;
    @Expose
    private long interval;
    @Expose
    private String linkTitle;
    @Expose
    private int forcedLogin; // 是否需要登录 1需要登录

    @Expose
    private String keyword; // 关键词
    @Expose
    private String catalogId;
    @Expose
    private String brandId;
    @Expose
    private String popproductid;
    @Expose
    private String activityTitle; // 活动标题
    @Expose
    private String activityPic; // 活动bg(气泡背景)

    private int position = -1; // 统计位置的作用，表示第几个元素

    private int yizhenClickType = -1; //首页 0表示问诊，1表示处方单

    @Expose
    public String secrettitle;

    @Expose
    public String secretcontent1;

    @Expose
    public String secretcontent2;

    @Expose
    public String categoryId;

    @Expose
    private List<String> keywordList; // 搜索关键字集合

    @Expose
    private String floorLocation; // 为你推荐的广告的位置

    @Expose
    private String url; // 经营资质url

    public String getJumpLoginPage() {
        return jumpLoginPage;
    }

    public void setJumpLoginPage(String jumpLoginPage) {
        this.jumpLoginPage = jumpLoginPage;
    }

    private int insertFlag = -1; // 插入标识，1已经插入到列表中

    public String getContent() {
        return content;
    }

    public String getContentType() {
        if (!TextUtils.isEmpty(contentType)) {
            return contentType;
        }
        return "";

    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getBgType() {
        return bgType;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        if (!TextUtils.isEmpty(title)) {
            return title;
        }

        return "";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTriggerValue() {
        if (!TextUtils.isEmpty(triggerValue)) {
            return triggerValue;
        }

        return "";
    }

    public void setTriggerValue(String triggerValue) {
        this.triggerValue = triggerValue;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(int triggerType) {
        this.triggerType = triggerType;
    }

    public String getLookProductNum() {
        return lookProductNum;
    }

    public void setLookProductNum(String lookProductNum) {
        this.lookProductNum = lookProductNum;
    }

    public String getProductDetailTriggerShakeTime() {
        return productDetailTriggerShakeTime;
    }

    public void setProductDetailTriggerShakeTime(String productDetailTriggerShakeTime) {
        this.productDetailTriggerShakeTime = productDetailTriggerShakeTime;
    }

    public String getSearchTriggerShakeTime() {
        return searchTriggerShakeTime;
    }

    public void setSearchTriggerShakeTime(String searchTriggerShakeTime) {
        this.searchTriggerShakeTime = searchTriggerShakeTime;
    }

    public String getShakeTime() {
        return shakeTime;
    }

    public void setShakeTime(String shakeTime) {
        this.shakeTime = shakeTime;
    }

    public String getShowShakeTime() {
        return showShakeTime;
    }

    public void setShowShakeTime(String showShakeTime) {
        this.showShakeTime = showShakeTime;
    }

    public List<ItemContentVO> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemContentVO> itemList) {
        this.itemList = itemList;
    }

    public List<CategoryInfo> getCategoryinfo() {
        return categoryinfo;
    }

    public void setCategoryinfo(List<CategoryInfo> categoryinfo) {
        this.categoryinfo = categoryinfo;
    }

    public List<NameToProductsTemplate> getNameToProducts() {
        return nameToProducts;
    }

    public void setNameToProducts(List<NameToProductsTemplate> nameToProducts) {
        this.nameToProducts = nameToProducts;
    }

    public String getUnselectedPicture() {
        return unselectedPicture;
    }

    public void setUnselectedPicture(String unselectedPicture) {
        this.unselectedPicture = unselectedPicture;
    }

    public String getSelectedPicture() {
        return selectedPicture;
    }

    public void setSelectedPicture(String selectedPicture) {
        this.selectedPicture = selectedPicture;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public int getForcedLogin() {
        return forcedLogin;
    }

    public void setForcedLogin(int forcedLogin) {
        this.forcedLogin = forcedLogin;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getPopproductid() {
        return popproductid;
    }

    public void setPopproductid(String popproductid) {
        this.popproductid = popproductid;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getActivityPic() {
        return activityPic;
    }

    public void setActivityPic(String activityPic) {
        this.activityPic = activityPic;
    }

    public String getPic1() {
        return pic1;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    public String getPic2() {
        return pic2;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    public String getSubtitle() {
        if (!TextUtils.isEmpty(subtitle)) {
            return subtitle;
        }

        return "";
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getYizhenClickType() {
        return yizhenClickType;
    }

    public void setYizhenClickType(int yizhenClickType) {
        this.yizhenClickType = yizhenClickType;
    }

    public List<String> getKeywordList() {
        return keywordList;
    }

    public void setKeywordList(List<String> keywordList) {
        this.keywordList = keywordList;
    }


    public String getFloorLocation() {
        return floorLocation;
    }

    public void setFloorLocation(String floorLocation) {
        this.floorLocation = floorLocation;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public int getInsertFlag() {
        return insertFlag;
    }

    public void setInsertFlag(int insertFlag) {
        this.insertFlag = insertFlag;
    }

    public int getAppUserCenterAdSwitch() {
        return appUserCenterAdSwitch;
    }

    public HomeChangeContentBeanVO setAppUserCenterAdSwitch(int appUserCenterAdSwitch) {
        this.appUserCenterAdSwitch = appUserCenterAdSwitch;
        return this;
    }

    public int getAppOrderDetailSwitch() {
        return appOrderDetailSwitch;
    }

    public HomeChangeContentBeanVO setAppOrderDetailSwitch(int appOrderDetailSwitch) {
        this.appOrderDetailSwitch = appOrderDetailSwitch;
        return this;
    }
}
