package cn.pepedd.drive.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户vo
 *
 * @author pepedd864
 * @since 2023/9/14 9:08
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserVO implements Serializable {
  private String username;
  private String nickname;
  private String avatar;
  // 已全局配置时间格式化，亦可使用 @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")的方式
  private LocalDateTime createTime;
}
