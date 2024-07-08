package cn.pepedd.drive.entity.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录vo
 *
 * @since 2023/9/15 10:34
 * author pepedd864
 */
@Data
@Builder
public class LoginVO implements Serializable {
  private UserVO userVO;
}
