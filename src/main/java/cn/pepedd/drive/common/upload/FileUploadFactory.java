package cn.pepedd.drive.common.upload;

import cn.pepedd.drive.common.upload.impl.aliyun.AliOssUpload;
import cn.pepedd.drive.common.upload.impl.tencent.TencentOssUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 文件上传工厂类，获取各种文件上传实现类
 *
 * @author pepedd864
 * @since 2024/5/31
 */
@Component
@ConditionalOnProperty(name = "storage.enabled", havingValue = "true")
public class FileUploadFactory {
  @Autowired(required = false)
  private AliOssUpload aliOssUpload;
  @Autowired(required = false)
  private TencentOssUpload tencentOssUpload;
  @Value("${storage.use}")
  private String storageUse;

  public FileUploadProxy newInstance() {
    switch (storageUse) {
      case "tencent":
        return new FileUploadProxy(tencentOssUpload);
      case "aliyun":
      default:
        return new FileUploadProxy(aliOssUpload);
    }
  }

  public FileUploadProxy newInstance(String type) {
    switch (type) {
      case "tencent":
        return new FileUploadProxy(tencentOssUpload);
      case "aliyun":
      default:
        return new FileUploadProxy(aliOssUpload);
    }
  }
}
