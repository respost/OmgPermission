package net.zy13.library;

import android.Manifest;
import android.os.Build;

/**
 * 权限类
 * 将权限共分为11组，每组只要有一个权限申请成功，就默认整组权限都可以使用了。
 *
 * @author 安阳 QQ：15577969
 * @version 1.0
 * @team 美奇软件开发工作室
 * @date 2020/11/23 12:54
 */
public final class Permission {
    public static final String[] CALENDAR;
    public static final String[] CAMERA;
    public static final String[] CONTACTS;
    public static final String[] LOCATION;
    public static final String[] MICROPHONE;
    public static final String[] PHONE;
    public static final String[] SENSORS;
    public static final String[] SMS;
    public static final String[] STORAGE;
    //安装应用权限
    public static final String[] PACKAGES;
    //通知栏权限
    public static final String[] NOTIFICATION;
    //悬浮窗权限
    public static final String[] ALERTWINDOW;
    //系统设置权限
    public static final String[] SETTINGS;

    static {
        /**
         * Android系统从6.0开始将权限分为一般权限和危险权限：
         * 1、一般权限指不涉及用户隐私的一些权限，比如Internet权限。
         * 2、危险权限指涉及获取用户隐私的一些操作所需要的权限，比如读取用户地理位置的权限。
         * Android在对权限进行分类的同时，还将危险类型的权限进行了分组划分,因此我们在申请权限的时候要一组一组的申请。
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            CALENDAR = new String[]{};
            CAMERA = new String[]{};
            CONTACTS = new String[]{};
            LOCATION = new String[]{};
            MICROPHONE = new String[]{};
            PHONE = new String[]{};
            SENSORS = new String[]{};
            SMS = new String[]{};
            STORAGE = new String[]{};
            PACKAGES=new String[]{};
            NOTIFICATION=new String[]{};
            ALERTWINDOW=new String[]{};
            SETTINGS=new String[]{};
        } else {
            CALENDAR = new String[]{
                    Manifest.permission.READ_CALENDAR,
                    Manifest.permission.WRITE_CALENDAR};

            CAMERA = new String[]{
                    Manifest.permission.CAMERA};

            CONTACTS = new String[]{
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.GET_ACCOUNTS};
            //Android10及以上版本，新增2种权限
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                LOCATION = new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,//在后台获取位置（Android 10.0及以上）
                        Manifest.permission.ACCESS_MEDIA_LOCATION//读取照片中的地理位置（Android 10.0及以上）
                };
            }else{
                LOCATION = new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION};
            }

            MICROPHONE = new String[]{
                    Manifest.permission.RECORD_AUDIO};

            //Android8以上版本PROCESS_OUTGOING_CALLS换成了ANSWER_PHONE_CALLS。
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                PHONE = new String[]{
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.WRITE_CALL_LOG,
                        Manifest.permission.USE_SIP,
                        Manifest.permission.ADD_VOICEMAIL,
                        Manifest.permission.ANSWER_PHONE_CALLS,//接听电话（Android8.0及以上）
                        Manifest.permission.READ_PHONE_NUMBERS//读取手机号码（Android8.0及以上）
                };
            }else {
                PHONE = new String[]{
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.WRITE_CALL_LOG,
                        Manifest.permission.USE_SIP,
                        Manifest.permission.ADD_VOICEMAIL,
                        Manifest.permission.PROCESS_OUTGOING_CALLS};
            }

            SENSORS = new String[]{
                    Manifest.permission.BODY_SENSORS,
                    Manifest.permission.ACTIVITY_RECOGNITION};

            SMS = new String[]{
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_WAP_PUSH,
                    Manifest.permission.RECEIVE_MMS};

            /**
             * 外部存储权限
             * Android11以上版本，存储权限统一用MANAGE_EXTERNAL_STORAGE
             */
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                STORAGE = new String[]{
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE};
            }else {
                STORAGE = new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};
            }
            //安装应用权限（Android8.0及以上）
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                PACKAGES=new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES};
            }else{
                PACKAGES = new String[]{};
            }
            //通知栏权限
            NOTIFICATION=new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY};
            //悬浮窗权限
            ALERTWINDOW=new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW};
            //系统设置权限
            SETTINGS=new String[]{Manifest.permission.WRITE_SETTINGS};
        }
    }
}
