package cn.pepedd.drive.exception;

import cn.pepedd.drive.common.enums.ErrorCode;

/**
 * 业务异常
 *
 * @author pepedd864
 * @since 2024/5/31
 */
public class BusinessException extends RuntimeException {
  /**
   * 错误码
   */
  private final int code;

  public BusinessException(int code, String message) {
    super(message);
    this.code = code;
  }

  public BusinessException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.code = errorCode.getCode();
  }

  public BusinessException(ErrorCode errorCode, String message) {
    super(message);
    this.code = errorCode.getCode();
  }

  public int getCode() {
    return code;
  }
}
