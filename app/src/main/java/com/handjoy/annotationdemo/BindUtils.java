package com.handjoy.annotationdemo;

import android.app.Activity;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BindUtils {

    private final static String TAG="bindUtils";

    private final static String SUFFIX="_ViewBinding";

    public static void bind(Object target){
        Log.e(TAG, target.getClass().getPackage().getName());
        String bindViewFile = target.getClass().getSimpleName().concat(SUFFIX);
        bindViewFile=target.getClass().getPackage().getName()
                .concat(".").concat(bindViewFile);

        Log.e(TAG,"bindView File Name:"+bindViewFile);

        try {
            Class<?> aClass = Class.forName(bindViewFile);
            Method inject = aClass.getDeclaredMethod("inject", target.getClass());
            inject.invoke(null,target);
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
