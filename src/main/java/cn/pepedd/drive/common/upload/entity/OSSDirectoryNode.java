package cn.pepedd.drive.common.upload.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * OSS生成文件夹节点
 *
 * @author pepedd864
 * @since 2024/8/2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OSSDirectoryNode {
  private String name;
  private String path;
  private String type;
  private long size;
  private Date lastModified;
  private List<OSSDirectoryNode> children;
}
