# Android权限动态请求框架
### 开发语言：Android
### 开发工具：Android Studio

1.关于Android6.0之后的运行时权限说明
---

Google在Android 6.0开始引入了权限申请机制，将所有权限分成了正常权限和危险权限。我们知道在6.0之前的版本只需把APP需要使用的权限罗列出来告知用户一下即可，而6.0以后如果权限涉及到隐私就需要用户进行授权才能使用，比如访问摄像机，读取sd卡等，这就是运行时权限；相反不涉及用户隐私，是不需要用户进行授权的，这是一般权限，比如访问网络，震动等。 

2.权限等级
---

权限主要分为normal、dangerous、signature和signatureOrSystem四个等级，常规情况下我们只需要了解前两种，即正常权限和危险权限。

* 2.1、正常权限

正常权限不会直接给用户隐私权带来风险。如果您的应用在其清单中列出了正常权限，系统将自动授予该权限。

* 2.2、危险权限

危险权限会授予应用访问用户机密数据的权限。如果您的应用在其清单中列出了正常权限，系统将自动授予该权限。如果您列出了危险权限，则用户必须明确批准您的应用使用这些权限。

* 2.3、危险权限分组列表

|分组|Permissions|
|:-----------:|:-----------:|
|CALENDAR|android.permission.READ_CALENDAR<br>android.permission.WRITE_CALENDAR|
|CAMERA|android.permission.CAMERA|
|CONTACTS|android.permission.READ_CONTACTS<br>android.permission.WRITE_CONTACTS<br>android.permission.GET_ACCOUNTS|
|LOCATION|android.permission.ACCESS_FINE_LOCATION<br>android.permission.ACCESS_COARSE_LOCATION|
|MICROPHONE|android.permission.RECORD_AUDIO|
|PHONE|android.permission.READ_PHONE_STATE<br>android.permission.CALL_PHONE<br>android.permission.READ_CALL_LOG<br>android.permission.WRITE_CALL_LOG<br>com.android.voicemail.permission.ADD_VOICEMAIL<br>android.permission.USE_SIP<br>android.permission.PROCESS_OUTGOING_CALLS|
|SENSORS|android.permission.BODY_SENSORS|
|SMS|android.permission.SEND_SMS<br>android.permission.RECEIVE_SMS<br>android.permission.READ_SMS<br>android.permission.RECEIVE_WAP_PUSH<br>android.permission.RECEIVE_MMS<br>android.permission.READ_CELL_BROADCASTS|
|STORAGE|android.permission.READ_EXTERNAL_STORAGE<br>android.permission.WRITE_EXTERNAL_STORAGE|

3、运行时请求权限
---

* 3.1、检查权限

应用每次需要危险权限时，都要判断应用目前是否有该权限，兼容库已经做好了封装，我们直接用如下方式调用即可：

```Java
int permissionCheck = ContextCompat.checkSelfPermission(thisActivity,    Manifest.permission.WRITE_CALENDAR);
```

 如果有权限则返回PackageManager.PERMISSION_GRANTED，否则返回PackageManager。PERMISSION_DENIED。
 
* 3.2、请求权限

当应用需要某个权限时，可以申请获取权限，这时会有弹出一个系统标准Dialog提示申请权限，此Diolog不能定制，用户同意或者拒绝后会通过方法onRequestPermissionsResult()返回结果。

```Java
 ActivityCompat.requestPermissions(thisActivity, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE);
``` 
 
* 3.3、处理权限请求响应

当用户处理权限请求后，系统会回调申请权限的Activity的onRequestPermissionsResult()方法，只需要覆盖此方法，就能获得返回结果。

```Java
 @Override
public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
 
}
```


4.框架使用示例：
---

* 4.1、添加依赖和配置：

```Java
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}

dependencies {
	implementation 'com.github.respost:OmgPermission:1.0'
}
```
* 4.2、一行代码实现权限请求，就是这么简单！

```Java
/**
 * 请求权限
 * request()方法的参数可以有也可以没有，有且不为空，就会回调PermissionCallback的响应的回调方法，没有或为空，则回调响应的注解方法。
 */
OmgPermission.with(MainActivity.this)
    ////添加请求码
    .addRequestCode(100)
    //单独申请一个权限
    //.permissions(Manifest.permission.CAMERA)
    //同时申请多个权限
    .permissions(Manifest.permission.READ_CONTACTS, Manifest.permission.RECEIVE_SMS, Manifest.permission.WRITE_CONTACTS)
    .request(new OmgPermission.PermissionCallback(){
        @Override
        public void permissionSuccess(int requestCode) {
            Toast.makeText(MainActivity.this, "成功授予联系人权限，请求码： " + requestCode, Toast.LENGTH_SHORT).show();
        }
        @Override
        public void permissionFail(int requestCode) {
            Toast.makeText(MainActivity.this, "授予联系人权限失败，请求码： " + requestCode, Toast.LENGTH_SHORT).show();
        }
    });
```

* 4.3、回调注解方法：

```Java
/**
 * 回调注解方法
 * 当request()没有参数的时候，就会在当前类里面寻找相应的注解方法
 */
@PermissionSuccess(requestCode = 100)
public void permissionSuccess() {
    Toast.makeText(MainActivity.this, "回调注解方法：成功授予读写权限" , Toast.LENGTH_SHORT).show();
}
```

* 4.4、申请权限的系统回调方法:

```Java
/**
 * 申请权限的系统回调方法
 * @param requestCode
 * @param permissions
 * @param grantResults
*/
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    OmgPermission.onRequestPermissionsResult(MainActivity.this, requestCode, permissions, grantResults);
}
```

* 4.5、使用needPermission方法申请权限:

```Java
/**
 * 请求权限
 * 如果没有callback作为参数，就会去调用响应的注解方法
 */
OmgPermission.needPermission(MainActivity.this, REQUEST_STORAGE, Permission.STORAGE);
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
```