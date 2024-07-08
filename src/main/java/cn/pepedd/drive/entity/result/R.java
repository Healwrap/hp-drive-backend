package cn.pepedd.drive.entity.result;

import cn.hutool.json.JSONUtil;
import cn.pepedd.drive.common.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * 统一返回类
 *
 * @author pepedd864
 * @since 2023/8/22 8:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> implements Serializable {


  private int code;
  private String msg;
  private T data;

  public R(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }


  public static <T> R<T> success() {
    return new R<>(ErrorCode.SUCCESS.getCode(), null);
  }


  public static <T> R<T> success(T object) {
    return new R<>(ErrorCode.SUCCESS.getCode(), null, object);
  }

  public static <T> R<T> error(ErrorCode errorCode) {
    return new R<>(errorCode.getCode(), errorCode.getMessage());
  }

  public static <T> R<T> error(int code, String msg) {
    return new R<>(code, msg);
  }


  public static R error(ErrorCode errorCode, String msg) {
    return new R(errorCode.getCode(), msg);
  }


  /**
   * 重写toString方法, 默认返回json字符串
   *
   * @return
   */
  @Override
  public String toString() {
    return JSONUtil.toJsonStr(this);
  }

  /**
   * 转换为Map
   *
   * @return
   */
  public Map<String, Object> toMap() {
    return JSONUtil.toBean(JSONUtil.toJsonStr(this), Map.class);
  }
}
