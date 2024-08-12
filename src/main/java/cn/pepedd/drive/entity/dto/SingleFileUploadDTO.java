package cn.pepedd.drive.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 小文件上传
 *
 * @author pepedd864
 * @since 2024/8/4
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SingleFileUploadDTO implements Serializable {
  private Long parentId;
  @NotBlank(message = "文件名不能为空")
  private String fileName;
  @NotBlank(message = "文件md5不能为空")
  private String md5;
  @NotNull(message = "文件不能为空")
  private MultipartFile file;
  @NotBlank(message = "文件路径不能为空")
  private String filePath;
}
