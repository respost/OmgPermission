package net.zy13.library;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.fragment.app.Fragment;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限的工具类
 *
 * @author 安阳 QQ：15577969
 * @version 1.0
 * @team 美奇软件开发工作室
 * @date 2020/11/23 13:09
 */
public class PermissionUtils {
    /**
     * 判断Android系统版本是否大于6.0
     *
     * @return
     */
    public static boolean judgeVersion() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 从申请的权限中找出未授予的权限
     *
     * @param activity
     * @param permission
     * @return
     */
    @TargetApi(value = Build.VERSION_CODES.M)
    public static List<String> findDeniedPermissions(Activity activity, String... permission) {
        List<String> denyPermissions = new ArrayList<>();
        for (String value : permission) {
            if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED) {
                denyPermissions.add(value);
            }
        }
        return denyPermissions;
    }

    /**
     * 寻找相应的注解方法
     *
     * @param c1 要寻找的那个类
     * @param c2 响应的注解标记
     * @return
     */
    public static List<Method> findAnnotationMethods(Class c1, Class<? extends Annotation> c2) {
        List<Method> methods = new ArrayList<>();
        for (Method method : c1.getDeclaredMethods()) {
            if (method.isAnnotationPresent(c2)) {
                methods.add(method);
            }
        }
        return methods;
    }

    public static <A extends Annotation> Method findMethodPermissionFailWithRequestCode(Class clazz, Class<A> permissionFailClass, int requestCode) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(permissionFailClass)) {
                if (requestCode == method.getAnnotation(PermissionFail.class).requestCode()) {
                    return method;
                }
            }
        }
        return null;
    }

    /**
     * 找到相应的注解方法（requestCode请求码与需要的一样）
     *
     * @param m
     * @param c
     * @param requestCode
     * @return
     */
    public static boolean isEqualRequestCodeFromAnntation(Method m, Class c, int requestCode) {
        if (c.equals(PermissionFail.class)) {
            return requestCode == m.getAnnotation(PermissionFail.class).requestCode();
        } else if (c.equals(PermissionSuccess.class)) {
            return requestCode == m.getAnnotation(PermissionSuccess.class).requestCode();
        } else {
            return false;
        }
    }

    public static <A extends Annotation> Method findMethodWithRequestCode(Class c, Class<A> annotation, int requestCode) {
        for (Method method : c.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                if (isEqualRequestCodeFromAnntation(method, annotation, requestCode)) {
                    return method;
                }
            }
        }
        return null;
    }

    public static <A extends Annotation> Method findMethodPermissionSuccessWithRequestCode(Class c, Class<A> permissionFailClass, int requestCode) {
        for (Method method : c.getDeclaredMethods()) {
            if (method.isAnnotationPresent(permissionFailClass)) {
                if (requestCode == method.getAnnotation(PermissionSuccess.class).requestCode()) {
                    return method;
                }
            }
        }
        return null;
    }

    public static Activity getActivity(Object object) {
        if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        } else if (object instanceof Activity) {
            return (Activity) object;
        }
        return null;
    }
}