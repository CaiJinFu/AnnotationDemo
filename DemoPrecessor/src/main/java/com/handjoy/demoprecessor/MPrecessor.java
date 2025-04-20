package com.handjoy.demoprecessor;

import com.google.auto.service.AutoService;
import com.handjoy.demoannotation.BindView;
import com.handjoy.demoannotation.OnClick;
import com.squareup.javapoet.MethodSpec;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * 注解处理器实现类，用于处理BindView和OnClick注解
 * 继承自AbstractProcessor，使用AutoService自动注册
 */
@AutoService(Processor.class)
public class MPrecessor extends AbstractProcessor {

    /**
     * 文件生成工具
     */
    private Filer filerUtils;

    /**
     * 元素处理工具
     */
    private Elements elementUtils;

    /**
     * 消息打印工具
     */
    private Messager messagerUtils;

    /**
     * 配置选项
     */
    private Map<String, String> options;

    /**
     * 处理器辅助类
     */
    private ProcessorHelper helper;

    /**
     * 初始化处理器
     *
     * @param processingEnvironment 处理环境对象
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        // 初始化各种工具类
        filerUtils = processingEnvironment.getFiler();
        elementUtils = processingEnvironment.getElementUtils();
        messagerUtils = processingEnvironment.getMessager();
        options = processingEnvironment.getOptions();
        helper = new ProcessorHelper();
    }

    /**
     * 获取支持的注解类型
     *
     * @return 支持的注解类型集合
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        // 添加BindView和OnClick注解支持
        types.add(BindView.class.getCanonicalName());
        types.add(OnClick.class.getCanonicalName());
        return types;
    }

    /**
     * 处理注解的主方法
     *
     * @param set 需要处理的注解类型集合
     * @param roundEnvironment 当前轮次的环境
     * @return 是否处理完成
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        // 处理BindView注解
        processBindView(roundEnvironment);
        // 处理OnClick注解
        processOnClick(roundEnvironment);
        try {
            // 生成最终文件
            helper.createFiles(filerUtils);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 处理BindView注解
     *
     * @param roundEnvironment 当前轮次的环境
     */
    private void processBindView(RoundEnvironment roundEnvironment) {
        // 遍历所有带有BindView注解的元素
        for (Element element : roundEnvironment.getElementsAnnotatedWith(BindView.class)) {
            // 只处理字段类型的元素
            if (element.getKind() == ElementKind.FIELD) {
                // 获取包裹元素和包信息
                Element enclosingElement = element.getEnclosingElement();
                PackageElement packageOf = elementUtils.getPackageOf(enclosingElement);
                String key = packageOf.toString() + enclosingElement.getSimpleName().toString();
                BindView annotation = element.getAnnotation(BindView.class);
                // 获取或创建处理器Bean
                ProcessorBean processor = helper.getOrEmpty(key);
                processor.setFileName(enclosingElement.getSimpleName().toString() + "_ViewBinding");
                processor.setPackageName(packageOf.toString());
                processor.setTargetName(enclosingElement.getSimpleName().toString());
                // 构建参数和方法
                helper.checkAndBuildParameter(processor);
                helper.checkAndBuildInject(processor);
                // 添加findViewById语句
                processor.setmInjectMethod(processor.getmInjectMethod().toBuilder()
                    .addStatement("$L.$L=$L.findViewById($L)",
                        enclosingElement.getSimpleName().toString().toLowerCase(),
                        element.getSimpleName().toString(),
                        enclosingElement.getSimpleName().toString().toLowerCase(),
                        annotation.value()).build());

            } else {
                // 不支持的类型打印错误
                logE("bindview", "@BindView not support this kind %s", element.getKind());
            }
        }
    }

    /**
     * 处理OnClick注解
     *
     * @param roundEnvironment 当前轮次的环境
     */
    private void processOnClick(RoundEnvironment roundEnvironment) {
        // 遍历所有带有OnClick注解的元素
        for (Element element : roundEnvironment.getElementsAnnotatedWith(OnClick.class)) {
            // 只处理方法类型的元素
            if (element.getKind() == ElementKind.METHOD) {
                // 获取包裹元素和包信息
                Element enclosingElement = element.getEnclosingElement();
                PackageElement packageOf = elementUtils.getPackageOf(enclosingElement);
                String key = packageOf.toString() + enclosingElement.getSimpleName().toString();
                OnClick annotation = element.getAnnotation(OnClick.class);
                // 获取或创建处理器Bean
                ProcessorBean processor = helper.getOrEmpty(key);
                processor.setFileName(enclosingElement.getSimpleName().toString() + "_ViewBinding");
                processor.setPackageName(packageOf.toString());
                processor.setTargetName(enclosingElement.getSimpleName().toString());
                // 构建参数和方法
                helper.checkAndBuildParameter(processor);
                helper.checkAndBuildInject(processor);
                MethodSpec methodSpec = processor.getmInjectMethod();
                // 为每个id添加点击监听器
                for (int id : annotation.value()) {
                    methodSpec = methodSpec.toBuilder().addStatement(
                        "$L.findViewById($L).setOnClickListener(new $T.OnClickListener() {\n" +
                            "      @Override\n" + "      public void onClick(View v) {\n" +
                            "        $L.$L(v);\n" + "      }\n" + "    })\n",
                        enclosingElement.getSimpleName().toString().toLowerCase(), id,
                        MConstants.CLASSNAME_VIEW,
                        enclosingElement.getSimpleName().toString().toLowerCase(),
                        element.getSimpleName().toString()).build();
                }
                processor.setmInjectMethod(methodSpec);

            } else {
                // 不支持的类型打印错误
                logE("OnClick", "@OnClick not support this kind %s", element.getKind());
            }
        }
    }

    /**
     * 打印错误日志
     *
     * @param tag 日志标签
     * @param format 格式化字符串
     * @param objs 格式化参数
     */
    private void logE(String tag, String format, Object... objs) {
        messagerUtils.printMessage(Diagnostic.Kind.ERROR, String.format(format, objs));
    }

}

