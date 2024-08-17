package cn.pepedd.drive;

import cn.pepedd.drive.common.upload.impl.aliyun.AliOssUpload;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * TODO
 *
 * @author pepedd864
 * @since 2024/8/2
 */
@SpringBootTest
public class TestClass {
  @Autowired(required = false)
  private AliOssUpload aliOssUpload;

  @Test
  public void testOSSList() {
    // 1. 初始化分片上传
    String uploadId = aliOssUpload.initMultipart("test");
    // 2. 上传分片

  }
}
