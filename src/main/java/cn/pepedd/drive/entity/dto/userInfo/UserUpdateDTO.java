package cn.pepedd.drive.entity.dto.userInfo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 用户更新信息
 */
@Data
public class UserUpdateDTO implements Serializable {
  @Length(min = 4, max = 12, message = "昵称长度为4-12")
  private String nickname;
  @Pattern(regexp = "^1[3|4|5|7|8][0-9]\\d{8}$", message = "手机号格式不正确")
  private String phone;
  @Email(message = "邮箱格式不正确")
  private String email;
}
