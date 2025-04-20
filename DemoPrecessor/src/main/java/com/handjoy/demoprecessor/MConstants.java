package com.handjoy.demoprecessor;

import com.squareup.javapoet.ClassName;

/**
 * 注解处理器常量类
 * 包含注解处理器中使用的各种常量定义
 */
public class MConstants {

    /**
     * Android Log工具类的ClassName引用
     */
    public static ClassName CLASSNAME_LOG = ClassName.get("android.util", "Log");

    /**
     * Android UI线程注解的ClassName引用
     */
    public static ClassName CLASSNAME_UI_THREAD = ClassName.get("android.support.annotation", "UiThread");

    /**
     * Android View类的ClassName引用
     */
    public static ClassName CLASSNAME_VIEW = ClassName.get("android.view", "View");

    /**
     * 自动生成的方法名常量
     */
    public static String INJECT_NAME = "inject";

}

