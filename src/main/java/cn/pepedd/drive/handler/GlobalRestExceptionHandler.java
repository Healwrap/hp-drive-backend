package cn.pepedd.drive.handler;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.exception.SaTokenException;
import cn.hutool.core.util.StrUtil;
import cn.pepedd.drive.common.enums.ErrorCode;
import cn.pepedd.drive.entity.result.R;
import cn.pepedd.drive.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;

/**
 * Rest全局异常处理器
 *
 * @author pepedd864
 * @since 2023/11/3
 */
@Slf4j
@RestControllerAdvice
public class GlobalRestExceptionHandler {
  /**
   * 处理所有不可知的异常
   */
  @ExceptionHandler
  public R exceptionHandler(Exception e) {
    e.printStackTrace();
    return R.error(ErrorCode.SYSTEM_ERROR, "系统异常");
  }

  /**
   * 处理SaToken 鉴权异常
   */
  @ExceptionHandler(SaTokenException.class)
  public R handleSaTokenException(SaTokenException e) {
    e.printStackTrace();
    String msg = e.getMessage();
    // 未登录异常
    if (e instanceof NotLoginException) {
      return R.error(ErrorCode.NOT_LOGIN_ERROR, msg);
    }
    // 无权限异常
    if (e instanceof NotRoleException) {
      return R.error(ErrorCode.NO_AUTH_ERROR, "无权限");
    }
    // 其他异常
    if (StrUtil.isBlankIfStr(msg)) {
      return R.error(ErrorCode.SYSTEM_ERROR, "鉴权异常");
    }
    return R.error(ErrorCode.SYSTEM_ERROR, msg);
  }

  /**
   * 处理键重复异常
   */
  @ExceptionHandler(DuplicateKeyException.class)
  public R handleDuplicateKeyException(DuplicateKeyException e) {
    e.printStackTrace();
    return R.error(ErrorCode.PARAMS_ERROR, "数据重复，请检查后提交");
  }

  /**
   * 业务异常
   */
  @ExceptionHandler(BusinessException.class)
  public R businessExceptionHandler(BusinessException e) {
    e.printStackTrace();
    return R.error(e.getCode(), e.getMessage());
  }

  /**
   * 处理参数校验异常
   */
  @ExceptionHandler(value = {BindException.class, ValidationException.class, MethodArgumentNotValidException.class})
  public R handleValidatorException(Exception e) {
    e.printStackTrace();
    StringBuilder sb = new StringBuilder("校验失败:[");
    if (e instanceof BindException) {
      BindException exception = (BindException) e;
      BindingResult bindingResult = exception.getBindingResult();
      for (FieldError fieldError : bindingResult.getFieldErrors()) {
        sb.append(fieldError.getDefaultMessage()).append(";");
      }
    } else if (e instanceof ValidationException) {
      ValidationException exception = (ValidationException) e;
      sb.append(exception.getMessage());
    } else if (e instanceof MethodArgumentNotValidException) {
      MethodArgumentNotValidException exception = (MethodArgumentNotValidException) e;
      sb.append(exception.getBindingResult().getFieldError().getDefaultMessage());
    }
    sb.append("]");
    return R.error(ErrorCode.PARAMS_ERROR, sb.toString());
  }
}
