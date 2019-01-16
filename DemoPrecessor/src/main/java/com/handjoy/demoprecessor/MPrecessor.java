package com.handjoy.demoprecessor;

import com.google.auto.service.AutoService;
import com.handjoy.demoannotation.MFirstAnnotation;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
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
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class MPrecessor extends AbstractProcessor {
    private Filer filerUtils; //
    private Elements elementUtils; //
    private Messager messagerUtils; //
    private Map<String, String> options; //

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
                messagerUtils.printMessage(Diagnostic.Kind.OTHER,"ready go");
                Name clzName = element.getSimpleName();
                Set<Modifier> modifiers = element.getModifiers();
                TypeMirror typeMirror = element.asType();
                ElementKind kind = element.getKind();
                List<? extends Element> enclosedElements = element.getEnclosedElements();
                Element enclosingElement = element.getEnclosingElement();

                String msg=String.format("SimpleName:%s,modifiers:%s,typeMirror:%s,kind:%s,enclosedElements:%s,enclosingElement:%s",
                        clzName,modifiers,typeMirror,kind,enclosedElements,enclosingElement);
                messagerUtils.printMessage(Diagnostic.Kind.NOTE,msg);
//
                PackageElement packageOf = elementUtils.getPackageOf(element);
                Name simpleName = packageOf.getSimpleName();
                Name qualifiedName = packageOf.getQualifiedName();
                String pckMsg=String.format("simpleName:%s,QualifiedName:%s",simpleName,qualifiedName);
                messagerUtils.printMessage(Diagnostic.Kind.NOTE,pckMsg);

                MFirstAnnotation annotation=element.getAnnotation(MFirstAnnotation.class);


                MethodSpec main = MethodSpec.methodBuilder("inject")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(void.class)
                        .addParameter(String[].class, "args")
                        .addStatement("$T.out.println($S)", System.class, "View id value:"+annotation.value())
                        .build();

                TypeSpec helloWorld = TypeSpec.classBuilder("InjectImpl")
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addMethod(main)
                        .build();

                JavaFile javaFile = JavaFile
                        .builder(qualifiedName.toString(), helloWorld)
                        .build();

                try {
                    javaFile.writeTo(filerUtils);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else {
                messagerUtils.printMessage(Diagnostic.Kind.ERROR,"not support");
            }
        }

        return false;
    }
}
