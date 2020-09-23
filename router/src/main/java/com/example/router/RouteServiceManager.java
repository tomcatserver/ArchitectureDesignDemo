package com.example.router;

import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.alibaba.android.arouter.launcher.ARouter;

public class RouteServiceManager {
    /**
     * 传入一个类的字节码和一个类的路径 返回一个类的实例对象
     *
     * @param clazz 类的字节码
     * @param path  路径
     * @param <T>   范型类
     * @return 返回一个类的实例对象
     */
    public static <T extends IProvider> T provide(Class<T> clazz, String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }

        IProvider provider = null;
        try {
            provider = (IProvider) ARouter.getInstance().build(path).navigation();
            return (T) provider;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T extends IProvider> T provide(Class<T> clazz) {
        IProvider provider = null;
        try {
            provider = (IProvider) ARouter.getInstance().navigation(clazz);
            return (T) provider;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 业务模块跳转
     *
     * @param path  目标路径
     * @param value 需要传入的参数
     * @param <T>   bundle的子类。
     */
    public static <T extends Bundle> void jumpPage(String path, T value) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        ARouter instance = ARouter.getInstance();
        if (instance != null) {
            Postcard build = instance.build(path);
            if (value == null) {
                build.navigation();
            } else {
                build.with(value).navigation();
            }
        }
    }
}
