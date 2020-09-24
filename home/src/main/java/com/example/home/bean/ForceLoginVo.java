package com.example.home.bean;


import java.io.Serializable;

public class ForceLoginVo implements Serializable {

    //强制登录开关（true:开 fasle:关）
    private boolean forceLoginSwitch;

    //商品信息表单开关
    private boolean goodsFormSwitch;

    //	身份证地址表单开关
    private boolean identityFormSwitch = true;

    public boolean isForceLoginSwitch() {
        return forceLoginSwitch;
    }

    public void setForceLoginSwitch(boolean forceLoginSwitch) {
        this.forceLoginSwitch = forceLoginSwitch;
    }

    public boolean isGoodsFormSwitch() {
        return goodsFormSwitch;
    }

    public void setGoodsFormSwitch(boolean goodsFormSwitch) {
        this.goodsFormSwitch = goodsFormSwitch;
    }

    public boolean isIdentityFormSwitch() {
        return identityFormSwitch;
    }

    public void setIdentityFormSwitch(boolean identityFormSwitch) {
        this.identityFormSwitch = identityFormSwitch;
    }

    @Override
    public String toString() {
        return "ForceLoginVo{" +
                "forceLoginSwitch=" + forceLoginSwitch +
                ", goodsFormSwitch=" + goodsFormSwitch +
                ", identityFormSwitch=" + identityFormSwitch +
                '}';
    }
}
