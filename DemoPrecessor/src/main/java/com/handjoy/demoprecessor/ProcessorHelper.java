package com.handjoy.demoprecessor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

/**
 * 注解处理器辅助类
 * 负责管理和构建注解处理过程中生成的代码元素
 */
public class ProcessorHelper {

    /**
     * 存储处理器Bean的映射表，key为类全限定名
     */
    private Map<String, ProcessorBean> builderMaps;

    /**
     * 构造函数，初始化映射表
     */
    public ProcessorHelper() {
        this.builderMaps = new HashMap<>();
    }

    /**
     * 添加处理器Bean到映射表
     *
     * @param key 类全限定名
     * @param processor 处理器Bean对象
     */
    public void put(String key, ProcessorBean processor) {
        builderMaps.put(key, processor);
    }

    /**
     * 获取指定key的处理器Bean，不存在则创建新实例
     *
     * @param key 类全限定名
     * @return 处理器Bean对象
     */
    public ProcessorBean getOrEmpty(String key) {
        if (builderMaps.get(key) == null) {
            put(key, new ProcessorBean());
        }
        return builderMaps.get(key);
    }

    /**
     * 创建并写入所有生成的Java文件
     *
     * @param filer 文件生成器
     * @throws IOException 文件操作异常
     */
    public void createFiles(Filer filer) throws IOException {
        for (ProcessorBean processor : builderMaps.values()) {
            checkAndBuildParameter(processor);
            checkAndBuildInject(processor);
            checkAndBuildClass(processor);
            checkAndBuildFile(processor);
            if (processor.getmFile() != null) {
                processor.getmFile().writeTo(filer);
            }
        }
    }

    /**
     * 检查并构建Java文件对象
     *
     * @param processor 处理器Bean对象
     */
    public void checkAndBuildFile(ProcessorBean processor) {
        if (processor.getmFile() != null) {
            return;
        }
        processor.setmFile(
            JavaFile.builder(processor.getPackageName(), processor.getmClass()).build());
    }

    /**
     * 检查并构建类定义
     *
     * @param processor 处理器Bean对象
     */
    public void checkAndBuildClass(ProcessorBean processor) {
        if (processor.getmClass() != null) {
            return;
        }
        processor.setmClass(
            TypeSpec.classBuilder(processor.getFileName()).addModifiers(Modifier.PUBLIC)
                .addMethod(processor.getmInjectMethod()).build());
    }

    /**
     * 检查并构建注入方法
     *
     * @param processor 处理器Bean对象
     */
    public void checkAndBuildInject(ProcessorBean processor) {
        if (processor.getmInjectMethod() != null) {
            return;
        }
        processor.setmInjectMethod(MethodSpec.methodBuilder(MConstants.INJECT_NAME)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(void.class)
            .addParameter(processor.getParameter()).addAnnotation(MConstants.CLASSNAME_UI_THREAD)
            .build());
    }

    /**
     * 检查并构建方法参数
     *
     * @param processor 处理器Bean对象
     */
    public void checkAndBuildParameter(ProcessorBean processor) {
        if (processor.getParameter() != null) {
            return;
        }
        ClassName targetClass =
            ClassName.get(processor.getPackageName(), processor.getTargetName());
        processor.setParameter(
            ParameterSpec.builder(targetClass, processor.getTargetName().toLowerCase())
                .addModifiers(Modifier.FINAL).build());
    }

}

