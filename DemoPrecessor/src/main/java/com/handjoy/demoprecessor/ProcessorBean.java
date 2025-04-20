package com.handjoy.demoprecessor;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

/**
 * 注解处理器数据封装类
 * 用于存储和传递注解处理过程中生成的各类元素
 */
public class ProcessorBean {

    /**
     * 生成的文件名
     */
    private String fileName;

    /**
     * 生成的类所在包名
     */
    private String packageName;

    /**
     * 目标类名（被注解的类名）
     */
    private String targetName;

    /**
     * 生成的注入方法
     */
    private MethodSpec mInjectMethod;

    /**
     * 生成的类定义
     */
    private TypeSpec mClass;

    /**
     * 生成的Java文件对象
     */
    private JavaFile mFile;

    /**
     * 方法参数定义
     */
    private ParameterSpec parameter;

    /**
     * 获取文件名
     *
     * @return 文件名
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 设置文件名
     *
     * @param fileName 文件名
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public MethodSpec getmInjectMethod() {
        return mInjectMethod;
    }

    public void setmInjectMethod(MethodSpec mInjectMethod) {
        this.mInjectMethod = mInjectMethod;
    }
    /**
     * 设置目标类名
     * @param targetName 目标类名
     */
    /**
     * 获取目标类名
     *
     * @return 目标类名
     */
    public TypeSpec getmClass() {
        return mClass;
    }

    public void setmClass(TypeSpec mClass) {
        this.mClass = mClass;
    }

    public JavaFile getmFile() {
        return mFile;
    }

    public void setmFile(JavaFile mFile) {
        this.mFile = mFile;
    }

    public ParameterSpec getParameter() {
        return parameter;
    }

    public void setParameter(ParameterSpec parameter) {
        this.parameter = parameter;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

}
