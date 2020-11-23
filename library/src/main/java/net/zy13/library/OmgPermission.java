package net.zy13.library;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.fragment.app.Fragment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 动态权限的对象
 * @author 安阳 QQ：15577969
 * @version 1.0
 * @team 美奇软件开发工作室
 * @date 2020/11/23 12:56
 */
public class OmgPermission {
    //权限集合
    private String[] mPermissions;
    //请求码
    private int mRequestCode;
    //对象
    private Object object;
    //权限回调方法
    private static PermissionCallback permissionCallback;

    /**
     * 构造方法
     */
    private OmgPermission(Object object) {
        this.object = object;
    }

    /**
     *  with函数是将某对象作为函数的参数，在函数块内可以通过 this 指代该对象。
     *  返回值为函数块的最后一行或指定return表达式。
     */
    public static OmgPermission with(Activity activity){
        return new OmgPermission(activity);
    }
    public static OmgPermission with(Fragment fragment){
        return new OmgPermission(fragment);
    }

    /**
     * 获取权限组集合
   * @param permissions
     * @return
     */
    public OmgPermission permissions(String... permissions){
        this.mPermissions = permissions;
        return this;
    }

    /**
     * 添加请求码
     * @param requestCode
     * @return
     */
    public OmgPermission addRequestCode(int requestCode){
        this.mRequestCode = requestCode;
        return this;
    }

    @TargetApi(value = Build.VERSION_CODES.M)
    public void request(){
        permissionCallback = null;
        requestPermissions(object, mRequestCode, mPermissions);
    }

    @TargetApi(value = Build.VERSION_CODES.M)
    public void request(PermissionCallback callback){
        if(callback!=null) {
            permissionCallback = callback;
        }
        requestPermissions(object, mRequestCode, mPermissions);
    }

    /**
     * 活动请求权限
     * @param activity
     * @param requestCode
     * @param permissions
     */
    public static void needPermission(Activity activity, int requestCode, String[] permissions){
        permissionCallback = null;
        requestPermissions(activity, requestCode, permissions);
    }

    public static void needPermission(Activity activity, int requestCode, String permission){
        permissionCallback = null;
        needPermission(activity, requestCode, new String[] { permission });
    }

    /**
     * 活动请求权限，带回调方法
     * @param activity
     * @param requestCode
     * @param permissions
     * @param callback
     */
    public static void needPermission(Activity activity, int requestCode, String[] permissions
            ,OmgPermission.PermissionCallback callback) {
        if (callback != null) {
            permissionCallback = callback;
        }
        requestPermissions(activity, requestCode, permissions);
    }

    public static void needPermission(Activity activity, int requestCode, String permission,PermissionCallback callback){
        if (callback != null) {
            permissionCallback = callback;
        }
        needPermission(activity, requestCode, new String[] { permission });
    }

    /**
     * 碎片请求权限
     * @param fragment
     * @param requestCode
     * @param permissions
     */
    public static void needPermission(Fragment fragment, int requestCode, String[] permissions){
        permissionCallback = null;
        requestPermissions(fragment, requestCode, permissions);
    }

    public static void needPermission(Fragment fragment, int requestCode, String permission){
        permissionCallback = null;
        needPermission(fragment, requestCode, new String[] { permission });
    }

    /**
     * 碎片请求权限，带回调方法
     * @param fragment
     * @param requestCode
     * @param permissions
     * @param callback
     */
    public static void needPermission(Fragment fragment, int requestCode, String[] permissions
            ,OmgPermission.PermissionCallback callback) {
        if (callback != null) {
            permissionCallback = callback;
        }
        requestPermissions(fragment, requestCode, permissions);
    }

    public static void needPermission(Fragment fragment, int requestCode, String permission,PermissionCallback callback){
        if (callback != null) {
            permissionCallback = callback;
        }
        needPermission(fragment, requestCode, new String[] { permission });
    }

    /**
     * 请求权限
     * @param object
     * @param requestCode
     * @param permissions
     */
    @TargetApi(value = Build.VERSION_CODES.M)
    private static void requestPermissions(Object object, int requestCode, String[] permissions){
        //判断系统版本是否大于6.0
        if(!PermissionUtils.judgeVersion()) {
            if (permissionCallback != null) {
                permissionCallback.permissionSuccess(requestCode);
            }else {
                doExecuteSuccess(object, requestCode);
            }
            return;
        }
        List<String> deniedPermissions = PermissionUtils.findDeniedPermissions(PermissionUtils.getActivity(object), permissions);

        /**
         * 先检查是否有没有授予的权限，有的话请求，没有的话就直接执行权限授予成功的接口/注解方法
         */
        if(deniedPermissions.size() > 0){
            if(object instanceof Activity){
                ((Activity)object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            } else if(object instanceof Fragment){
                ((Fragment)object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            } else {
                throw new IllegalArgumentException(object.getClass().getName() + " is not supported");
            }

        } else {
            if (permissionCallback != null) {
                permissionCallback.permissionSuccess(requestCode);
            }else {
                doExecuteSuccess(object, requestCode);
            }
        }
    }

    private static void doExecuteSuccess(Object activity, int requestCode) {
        Method executeMethod = PermissionUtils.findMethodWithRequestCode(activity.getClass(),
                PermissionSuccess.class, requestCode);

        executeMethod(activity, executeMethod);
    }

    private static void doExecuteFail(Object activity, int requestCode) {
        Method executeMethod = PermissionUtils.findMethodWithRequestCode(activity.getClass(),
                PermissionFail.class, requestCode);

        executeMethod(activity, executeMethod);
    }

    private static void executeMethod(Object activity, Method executeMethod) {
        if(executeMethod != null){
            try {
                if(!executeMethod.isAccessible()) executeMethod.setAccessible(true);
                executeMethod.invoke(activity, new  Object[]{});
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions,
                                                  int[] grantResults) {
        requestResult(activity, requestCode, permissions, grantResults);
    }

    public static void onRequestPermissionsResult(Fragment fragment, int requestCode, String[] permissions,
                                                  int[] grantResults) {
        requestResult(fragment, requestCode, permissions, grantResults);
    }

    /**
     * 回调接口不为空的话，先执行回调接口的方法，若为空，则寻找响应的注解方法。
     * @param obj
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    private static void requestResult(Object obj, int requestCode, String[] permissions,
                                      int[] grantResults){
        List<String> deniedPermissions = new ArrayList<>();
        for(int i=0; i<grantResults.length; i++){
            if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                deniedPermissions.add(permissions[i]);
            }
        }

        if(deniedPermissions.size() > 0){
            if(permissionCallback!=null){
                permissionCallback.permissionFail(requestCode);
            }else {
                doExecuteFail(obj, requestCode);
            }
        } else {
            if(permissionCallback!=null){
                permissionCallback.permissionSuccess(requestCode);
            }else {
                doExecuteSuccess(obj, requestCode);
            }
        }
    }

    public interface PermissionCallback{
        //请求权限成功
        void permissionSuccess(int requsetCode);

        //请求权限失败
        void permissionFail(int requestCode);
    }
}
