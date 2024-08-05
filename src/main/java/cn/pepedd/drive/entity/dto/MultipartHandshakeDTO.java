package cn.pepedd.drive.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 大文件上传握手
 *
 * @author pepedd864
 * @since 2024/8/5
 */
@Data
public class MultipartHandshakeDTO implements Serializable {
  private Long parentId;
  @NotBlank(message = "文件名不能为空")
  private String filename;
  @NotBlank(message = "文件路径不能为空")
  private String filePath;
  @NotBlank(message = "文件后缀不能为空")
  private String fileSuffix;
  @NotNull(message = "文件分块ids不能为空")
  private String[] chunkIds;
}
