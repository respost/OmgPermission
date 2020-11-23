package net.zy13.omg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import net.zy13.library.OmgPermission;
import net.zy13.library.Permission;
import net.zy13.library.PermissionFail;
import net.zy13.library.PermissionSuccess;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //联系人请求码
    private final int REQUEST_CONTACT = 100;
    //存储请求码
    private final int REQUEST_STORAGE = 200;
    //相机请求码
    private final int REQUEST_CAMERA = 300;

    private Button storageButton;
    private Button cameraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏，在加载布局之前设置(兼容Android2.3.3版本)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        /**
         * 请求权限
         * request()方法的参数可以有也可以没有，有且不为空，就会回调PermissionCallback的响应的回调方法，没有或为空，则回调响应的注解方法。
         */
        OmgPermission.with(MainActivity.this)
                ////添加请求码
                .addRequestCode(REQUEST_CAMERA)
                //单独申请一个权限
                //.permissions(Manifest.permission.CAMERA)
                //同时申请多个权限
                .permissions(Manifest.permission.READ_CONTACTS, Manifest.permission.RECEIVE_SMS, Manifest.permission.WRITE_CONTACTS)
                .request(new OmgPermission.PermissionCallback() {
                    @Override
                    public void permissionSuccess(int requestCode) {
                        Toast.makeText(MainActivity.this, "成功授予联系人权限，请求码： " + requestCode, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void permissionFail(int requestCode) {
                        Toast.makeText(MainActivity.this, "授予联系人权限失败，请求码： " + requestCode, Toast.LENGTH_SHORT).show();
                    }
                });
        initView();
    }

    private void initView() {
        //获取控件
        storageButton = (Button) findViewById(R.id.storageButton);
        cameraButton = (Button) findViewById(R.id.cameraButton);
        //设置监听
        storageButton.setOnClickListener(this);
        cameraButton.setOnClickListener(this);
    }

    /**
     * 回调注解方法
     * 当request()没有参数的时候，就会在当前类里面寻找相应的注解方法
     */
    @PermissionSuccess(requestCode = REQUEST_STORAGE)
    public void permissionSuccess() {
        Toast.makeText(MainActivity.this, "回调注解方法：成功授予读写权限", Toast.LENGTH_SHORT).show();
    }

    @PermissionFail(requestCode = REQUEST_STORAGE)
    public void permissionFail() {
        Toast.makeText(MainActivity.this, "回调注解方法：授予读写权限失败", Toast.LENGTH_SHORT).show();
    }

    @PermissionSuccess(requestCode = REQUEST_CONTACT)
    public void permissionSuccessContact() {
        Toast.makeText(MainActivity.this, "回调注解方法：成功授予联系人权限", Toast.LENGTH_SHORT).show();
    }

    @PermissionFail(requestCode = REQUEST_CONTACT)
    public void permissionFailContact() {
        Toast.makeText(MainActivity.this, "回调注解方法：授予联系人权限失败", Toast.LENGTH_SHORT).show();
    }

    @PermissionSuccess(requestCode = REQUEST_CAMERA)
    public void permissionSuccessCamera() {
        Toast.makeText(MainActivity.this, "回调注解方法：成功授予相机权限", Toast.LENGTH_SHORT).show();
    }

    @PermissionFail(requestCode = REQUEST_CAMERA)
    public void permissionFailCamera() {
        Toast.makeText(MainActivity.this, "回调注解方法：授予相机权限失败", Toast.LENGTH_SHORT).show();
    }

    /**
     * 申请权限的系统回调方法
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        OmgPermission.onRequestPermissionsResult(MainActivity.this, requestCode, permissions, grantResults);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //申请存储权限按钮
            case R.id.storageButton:
                /**
                 * 请求权限
                 * 如果没有callback作为参数，就会去调用响应的注解方法
                 */
                OmgPermission.needPermission(MainActivity.this, REQUEST_STORAGE, Permission.STORAGE);
                break;
            //申请相机权限按钮
            case R.id.cameraButton:
                /**
                 * 请求权限
                 */
                OmgPermission.needPermission(MainActivity.this, REQUEST_CAMERA, Permission.CAMERA, new OmgPermission.PermissionCallback() {
                    @Override
                    public void permissionSuccess(int requestCode) {
                        Toast.makeText(MainActivity.this, "成功授予相机权限", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void permissionFail(int requestCode) {
                        Toast.makeText(MainActivity.this, "授予相机权限失败", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }
}
