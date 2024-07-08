package cn.pepedd.drive.common.upload.impl.tencent;

import cn.pepedd.drive.common.upload.FileUpload;
import cn.pepedd.drive.common.upload.entity.OSSDirectoryNode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * TODO
 *
 * @author pepedd864
 * @since 2024/5/31
 */
@Component
@ConditionalOnProperty(name = "storage.use",havingValue = "tencent")
public class TencentOssUpload implements FileUpload {
  /**
   * 上传文件
   *
   * @param file
   * @return
   */
  @Override
  public String[] upload(MultipartFile... file) {
    return null;
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
    return null;
  }

  /**
   * 删除文件
   *
   * @param fileName
   * @return
   */
  @Override
  public Boolean delete(String... fileName) {
    return null;
  }

  /**
   * 生成文件树形结构
   *
   * @return
   */
  @Override
  public OSSDirectoryNode list() {
    return null;
  }

  /**
   * 下载文件夹（打包为压缩包下载）
   */
  @Override
  public void download() {

  }
}
