package cn.pepedd.drive.common.upload;

import cn.pepedd.drive.common.upload.entity.OSSDirectoryNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传代理类，用于记录日志
 *
 * @author pepedd864
 * @since 2024/5/31
 */
@Slf4j
public class FileUploadProxy implements FileUpload {
  private FileUpload fileUpload;

  public FileUploadProxy(FileUpload fileUpload) {
    this.fileUpload = fileUpload;
  }

  /**
   * 上传文件
   *
   * @param file
   * @return
   */
  @Override
  public String[] upload(MultipartFile... file) {
    log.info("上传文件...");
    String[] upload = fileUpload.upload(file);
    for (String s : upload) {
      log.info("上传文件成功，文件名：{}", s);
    }
    return upload;
  }

  /**
   * 为网盘项目定制的上传方法
   *
   * @param fileName
   * @param file
   * @return
   */
  @Override
  public Boolean uploadForDrive(String fileName, MultipartFile file) {
    return fileUpload.uploadForDrive(fileName, file);
  }

  /**
   * 删除文件
   *
   * @param fileName
   * @return
   */
  @Override
  public Boolean delete(String... fileName) {
    log.info("删除文件...");
    Boolean delete = fileUpload.delete(fileName);
    if (delete) {
      log.info("删除文件成功，文件名：{}", fileName);
    }
    return delete;
  }

  /**
   * 生成文件树形结构
   *
   * @return
   */
  @Override
  public OSSDirectoryNode list() {
    return fileUpload.list();
  }

  /**
   * 下载文件夹（打包为压缩包下载）
   */
  @Override
  public void download() {
    fileUpload.download();
  }
}
