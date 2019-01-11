package com.handjoy.demoprecessor;

import com.google.auto.service.AutoService;
import com.handjoy.demoannotation.MFirstAnnotation;

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
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class MPrecessor extends AbstractProcessor {
    private Filer filerUtils; // 文件写入
    private Elements elementUtils; // 操作Element工具类
    private Messager messagerUtils; // Log 日志
    private Map<String, String> options; // 额外配置参数

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        filerUtils = processingEnvironment.getFiler();
        elementUtils = processingEnvironment.getElementUtils();
        messagerUtils = processingEnvironment.getMessager();
        options = processingEnvironment.getOptions();
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types=new LinkedHashSet<>();
        types.add(MFirstAnnotation.class.getCanonicalName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(MFirstAnnotation.class);
        for (Element element : elementsAnnotatedWith) {
            if (element.getKind()==ElementKind.FIELD) {
                messagerUtils.printMessage(Diagnostic.Kind.OTHER,"准备好了，我要开始装逼了");
                String clzName = element.getSimpleName().toString();



            }else {
                messagerUtils.printMessage(Diagnostic.Kind.ERROR,"只支持对变量的修饰");
            }
        }

        return false;
    }
}
