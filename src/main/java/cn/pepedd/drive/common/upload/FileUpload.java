package cn.pepedd.drive.common.upload;

import cn.pepedd.drive.common.upload.entity.OSSDirectoryNode;
import org.springframework.web.multipart.MultipartFile;

/**
 * OSS文件上传接口
 *
 * @author pepedd864
 * @since 2024/5/31
 */
public interface FileUpload {
  /**
   * 上传文件
   *
   * @param file
   * @return
   */
  String[] upload(MultipartFile... file);

  /**
   * 为网盘项目定制的上传方法
   * @param fileName
   * @param file
   * @return
   */
  Boolean uploadForDrive(String fileName, MultipartFile file);

  /**
   * 删除文件
   *
   * @param fileName
   * @return
   */
  Boolean delete(String... fileName);

  /**
   * 生成文件树形结构
   * @return
   */
  OSSDirectoryNode list();

  /**
   * 下载文件夹（打包为压缩包下载）
   */
  void download();
}
