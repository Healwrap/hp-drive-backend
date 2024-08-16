package cn.pepedd.drive.common.upload.impl.aliyun;

import cn.pepedd.drive.common.enums.ErrorCode;
import cn.pepedd.drive.common.upload.FileUpload;
import cn.pepedd.drive.common.upload.entity.OSSDirectoryNode;
import cn.pepedd.drive.common.utils.FileUtils;
import cn.pepedd.drive.exception.BusinessException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 阿里云上传文件接口
 *
 * @author pepedd864
 * @since 2024/5/31
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "storage.use", havingValue = "aliyun")
public class AliOssUpload implements FileUpload {
  @Value("${storage.aliyun.project-name}")
  private String projectName;
  @Value("${storage.aliyun.endpoint}")
  private String endpoint;
  @Value("${storage.aliyun.access-key-id}")
  private String accessKeyId;
  @Value("${storage.aliyun.access-key-secret}")
  private String accessKeySecret;
  @Value("${storage.aliyun.bucket-name}")
  private String bucketName;
  @Value("${storage.aliyun.domain}")
  private String domain;
  private OSS oss;

  /**
   * TODO
   * Spring在AliOssUpload构造器中注入相关属性，而OSS依赖于AliOssUpload的属性
   * 因此需要在AliOssUpload构造完成成为Bean之后再初始化OSS
   * 所以使用@PostConstruct注解
   * 又因为OSS只是AliOssUpload内部需要使用，不需要暴露到Bean池中，所以不需要使用@Bean注解
   */
  @PostConstruct
  public void init() {
    oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
  }

  /**
   * 上传文件
   *
   * @param file
   * @return
   */
  @Override
  public String[] upload(MultipartFile... file) {
    // 初始化 urls 数组以存储每个文件的 URL
    String[] urls = new String[file.length];
    for (int i = 0; i < file.length; i++) {
      MultipartFile f = file[i];
      try {
        InputStream inputStream = f.getInputStream();
        String filename = f.getOriginalFilename();
        // 取后缀名
        String suffix = filename.substring(filename.lastIndexOf("."));
        // 使用 时间 生成文件名
        String newFileName = FileUtils.calculateFileMD5(inputStream) + suffix;
        String filePath = projectName + "/" + newFileName;
        if (!oss.doesObjectExist(bucketName, filePath)) {
          // 文件不存在，执行上传
          PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, filePath, inputStream);
          oss.putObject(putObjectRequest);
        }
        urls[i] = domain + "/" + projectName + "/" + newFileName;
      } catch (Exception e) {
        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传文件失败" + e.getMessage());
      }
    }
    // 返回 urls 数组
    return urls;
  }

  /**
   * 创建分片上传任务
   *
   * @param fileMd5 将文件md5作为文件名
   * @return 上传任务ID
   */
  public String initMultipart(String fileMd5) {
    InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, fileMd5);
    // 初始化分片。
    InitiateMultipartUploadResult upresult = oss.initiateMultipartUpload(request);
    return upresult.getUploadId();
  }

  /**
   * 上传分片
   *
   * @param uploadPart 分片文件流
   * @param uploadId   上传任务ID
   * @param fileMd5    文件md5
   * @param partNumber 分片序号
   * @param partSize   分片大小
   * @return
   */
  public PartETag uploadMultipart(InputStream uploadPart, String uploadId, String fileMd5, int partNumber, long partSize) {
    UploadPartRequest request = new UploadPartRequest();
    request.setUploadId(uploadId);
    request.setPartSize(partSize);
    request.setPartNumber(partNumber);
    request.setKey(fileMd5);
    request.setBucketName(bucketName);
    request.setInputStream(uploadPart);
    UploadPartResult uploadPartResult = oss.uploadPart(request);
    return uploadPartResult.getPartETag();
  }

  /**
   * 完成分片上传
   * @param fileMd5
   * @param uploadId
   * @param partETags
   */
  public void completeMultipart(String fileMd5, String uploadId, List<PartETag> partETags) {
    // 在执行完成分片上传操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
    CompleteMultipartUploadRequest completeMultipartUploadRequest =
        new CompleteMultipartUploadRequest(bucketName, fileMd5, uploadId, partETags);
    // 完成分片上传。
    oss.completeMultipartUpload(completeMultipartUploadRequest);
  }

  /**
   * 取消分片上传任务
   *
   * @param fileMd5
   * @param uploadId
   */
  public void abortMultipart(String fileMd5, String uploadId) {
    // 取消分片上传。
    AbortMultipartUploadRequest abortMultipartUploadRequest =
        new AbortMultipartUploadRequest(bucketName, fileMd5, uploadId);
    oss.abortMultipartUpload(abortMultipartUploadRequest);
  }

  /**
   * 列举出已上传的分片信息
   *
   * @param uploadId
   * @param fileMd5
   * @return
   */
  public PartListing listMultipart(String uploadId, String fileMd5) {
    // 列举所有已上传的分片。
    PartListing partListing;
    ListPartsRequest listPartsRequest = new ListPartsRequest(bucketName, fileMd5, uploadId);
    do {
      partListing = oss.listParts(listPartsRequest);
      // 指定List的起始位置，只有分片号大于此参数值的分片会被列出。
      listPartsRequest.setPartNumberMarker(partListing.getNextPartNumberMarker());
    } while (partListing.isTruncated());
    return partListing;
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
    try {
      InputStream inputStream = file.getInputStream();
      String filePath = projectName + "/" + fileName; // 保存到 oss 时不需要文件后缀，下载时全部单独处理
      if (!oss.doesObjectExist(bucketName, filePath)) {
        // 文件不存在，执行上传
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, filePath, inputStream);
        oss.putObject(putObjectRequest);
      } else {
        log.warn("文件：" + fileName + " AliyunOSS已上传过");
      }
    } catch (Exception e) {
      throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传文件失败" + e.getMessage());
    }
    return true;
  }

  /**
   * 删除文件
   *
   * @param fileName
   * @return
   */
  @Override
  public Boolean delete(String... fileName) {
    for (String f : fileName) {
      GenericRequest genericRequest = new GenericRequest(bucketName, projectName + f);
      VoidResult voidResult = oss.deleteObject(genericRequest);
    }
    return true;
  }

  public void download() {

  }

  /**
   * 获取OSS文件列表树（树形结构）
   * 不过这里存储只根据文件md5作为文件名，故存储为线性存储，不需要文件列表树
   *
   * @return
   */
  public OSSDirectoryNode list() {
    ObjectListing objectListing = oss.listObjects(bucketName, projectName);
    Map<String, OSSDirectoryNode> nodes = new HashMap<>();
    OSSDirectoryNode root = new OSSDirectoryNode(projectName, projectName, "directory", 0, null, new ArrayList<>()); // Remove trailing slash

    for (OSSObjectSummary summary : objectListing.getObjectSummaries()) {
      String path = summary.getKey();
      String[] parts = path.split("/");
      OSSDirectoryNode current = root;
      for (int i = 1; i < parts.length; i++) {
        String part = parts[i];
        OSSDirectoryNode child = nodes.get(current.getPath() + "/" + part);
        if (child == null) {
          String newPath = current.getPath() + "/" + part;
          String finalPath = i < parts.length - 1 ? newPath + "/" : newPath;
          child = new OSSDirectoryNode(part, finalPath, "directory", 0, null, new ArrayList<>());
          nodes.put(newPath, child);
          current.getChildren().add(child);
        }
        current = child;
      }
      current.setSize(summary.getSize());
      if (current.getSize() != 0) current.setType("file");
      current.setLastModified(summary.getLastModified());
    }
    return root;
  }
}
