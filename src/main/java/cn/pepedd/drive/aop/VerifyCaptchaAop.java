package cn.pepedd.drive.aop;

import cn.pepedd.drive.annotation.VerifyCaptcha;
import cn.pepedd.drive.common.cache.RedisCache;
import cn.pepedd.drive.common.constants.CacheConstants;
import cn.pepedd.drive.common.enums.ErrorCode;
import cn.pepedd.drive.exception.BusinessException;
import cn.pepedd.drive.exception.ThrowUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;

import static cn.pepedd.drive.common.constants.Constants.CAPTCHA_ENABLED;

/**
 * 验证码验证切面
 *
 * @author pepedd864
 * @since 2023/12/5
 */
@Aspect
@Component
@Slf4j
public class VerifyCaptchaAop {
  @Resource
  private RedisCache redisCache;


  /**
   * 校验验证码
   *
   * @param joinPoint     切点
   * @param verifyCaptcha 注解
   */
  @Before("@annotation(verifyCaptcha)")
  public void verifyCaptcha(JoinPoint joinPoint, VerifyCaptcha verifyCaptcha) {
    if (!CAPTCHA_ENABLED) return;
    log.info("校验验证码...");

    // 分割参数 0$code.n1.n2  0$uuid.n1.n2 为 0 code.n1.n2; 0 uuid.n1.n2 两个部分
    String[] split1 = verifyCaptcha.code().split("\\$");
    String[] split2 = verifyCaptcha.uuid().split("\\$");

    int n1 = Integer.parseInt(split1[0]);
    int n2 = Integer.parseInt(split2[0]);

    // 通过 args1[0] 中的数字n 获取第n个参数
    Object[] args = joinPoint.getArgs();
    String code = "";
    String uuid = "";

    try {
      // 一级参数时
      if (split1.length == 1) {
        code = args[n1].toString();
      }
      if (split2.length == 1) {
        uuid = args[n2].toString();
      }
      // 二级参数时
      if (split1.length != 1 && split2.length != 1) {
        if (args.length > n1) {
          code = getProperty(args[n1], split1[1]).toString();
        }
        if (args.length > n2) {
          uuid = getProperty(args[n2], split2[1]).toString();
        }
      }
    } catch (Exception e) {
      throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码参数错误");
    }

    log.info("code: {}", code);
    log.info("uuid: {}", uuid);

    // 校验验证码
    String captchaCode = redisCache.getCacheObject(CacheConstants.CAPTCHA_CODE_KEY + uuid);
    ThrowUtils.throwIf(captchaCode == null, ErrorCode.PARAMS_ERROR, "验证码已过期");
    ThrowUtils.throwIf(
        code == null || !code.equals(captchaCode),
        ErrorCode.PARAMS_ERROR,
        "验证码错误"
    );
    redisCache.deleteObject(CacheConstants.CAPTCHA_CODE_KEY + uuid);
  }

  /**
   * 通过反射获取对象属性值
   *
   * @param obj             对象
   * @param propertyNameStr 属性名，支持多级，如：user.name
   * @return 属性值
   * @throws Exception 异常
   */
  public Object getProperty(Object obj, String propertyNameStr) throws Exception {
    String[] propertyNames = propertyNameStr.split("\\.");

    Object propertyValue = obj;

    for (String propertyName : propertyNames) {
      Field field = propertyValue.getClass().getDeclaredField(propertyName);
      field.setAccessible(true);
      propertyValue = field.get(propertyValue);
    }

    return propertyValue;
  }
}
