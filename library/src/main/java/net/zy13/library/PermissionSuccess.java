package net.zy13.library;

/**
 * @author 安阳 QQ：15577969
 * @version 1.0
 * @team 美奇软件开发工作室
 * @date 2020/11/23 13:12
 */
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermissionSuccess {
    int requestCode();
}
