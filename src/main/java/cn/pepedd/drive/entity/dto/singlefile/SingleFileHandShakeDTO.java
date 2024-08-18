package cn.pepedd.drive.entity.dto.singlefile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 小文件上传握手
 *
 * @author pepedd864
 * @since 2024/8/18
 */


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SingleFileHandShakeDTO implements Serializable {
  private Long parentId;
  @NotBlank(message = "文件md5不能为空")
  private String md5;
  @NotBlank(message = "文件路径不能为空")
  private String filePath;
}
