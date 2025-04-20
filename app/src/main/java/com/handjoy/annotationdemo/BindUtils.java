package com.handjoy.annotationdemo;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 视图绑定工具类，用于通过反射动态绑定视图
 */
public class BindUtils {

    /**
     * 日志标签
     */
    private final static String TAG = "bindUtils";

    /**
     * 视图绑定类后缀
     */
    private final static String SUFFIX = "_ViewBinding";

    /**
     * 绑定目标对象的视图
     *
     * @param target 需要绑定视图的目标对象
     */
    public static void bind(Object target) {
        // 打印目标对象所在包名
        Log.e(TAG, target.getClass().getPackage().getName());
        // 构造视图绑定类的完整名称
        String bindViewFile = target.getClass().getSimpleName().concat(SUFFIX);
        bindViewFile = target.getClass().getPackage().getName().concat(".").concat(bindViewFile);
        // 打印视图绑定类的完整名称
        Log.e(TAG, "bindView File Name:" + bindViewFile);
        try {
            // 通过反射获取视图绑定类
            Class<?> aClass = Class.forName(bindViewFile);
            // 获取视图绑定类的inject方法
            Method inject = aClass.getDeclaredMethod("inject", target.getClass());
            // 调用静态的inject方法进行视图绑定
            inject.invoke(null, target);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}

