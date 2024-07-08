package cn.pepedd.drive.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 验证码校验注解
 *
 * @author pepedd864
 * @since 2023/12/5
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface VerifyCaptcha {
  String code() default "0"; // 验证码 默认为第一个参数

  String uuid() default "1"; // 验证码uuid 默认为第二个参数
}
