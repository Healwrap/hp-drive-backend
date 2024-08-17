package cn.pepedd.drive.service.impl;

import cn.pepedd.drive.common.enums.ErrorCode;
import cn.pepedd.drive.common.satoken.StpKit;
import cn.pepedd.drive.common.upload.FileUploadFactory;
import cn.pepedd.drive.common.upload.FileUploadProxy;
import cn.pepedd.drive.common.utils.FileUtils;
import cn.pepedd.drive.entity.dto.SingleFileUploadDTO;
import cn.pepedd.drive.entity.pojo.File;
import cn.pepedd.drive.entity.vo.FileVO;
import cn.pepedd.drive.exception.BusinessException;
import cn.pepedd.drive.mapper.FileMapper;
import cn.pepedd.drive.service.FileService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @description 针对表【tb_file(文件信息表)】的数据库操作Service实现
 * @createDate 2024-08-02 14:23:26
 */
@Service
@Slf4j
public class FileServiceImpl extends ServiceImpl<FileMapper, File>
    implements FileService {
  @Autowired(required = false)
  private FileUploadFactory fileUploadFactory;

  /**
   * 判断文件是否已上传
   *
   * @param md5
   * @return
   */
  private Boolean isUploaded(String md5) {
    return this.getBaseMapper().selectCount(new LambdaQueryWrapper<File>().eq(File::getMd5, md5)) > 0;
  }

  /**
   * 握手，主要是确定文件是否已上传 并创建文件夹记录
   *
   * @param uploadDTO
   * @return
   */
  public Boolean singleFileHandleShake(SingleFileUploadDTO uploadDTO) {

    return false;
  }

  /**
   * 小文件上传
   *
   * @param uploadDTO
   * @return
   */
  @Override
  public Boolean singleFileUpload(SingleFileUploadDTO uploadDTO) {
    try {
      // 计算MD5
      String fileMD5 = FileUtils.calculateFileMD5(uploadDTO.getFile().getInputStream());
      // 如果已经上传过，不再上传
      if (isUploaded(fileMD5)) {
        log.info("文件已上传过，无需重复上传");
        return true;
      }
      // 处理参数
      String fileName = uploadDTO.getFile().getOriginalFilename();
      String fileSuffix = FileUtils.getFileExtension(fileName);
      Long parentId = uploadDTO.getParentId();
      Long loginId = Long.parseLong((String) StpKit.USER.getLoginId());
      // 写入数据库
      // 首先根据filepath生成对应的数据库记录
      String path = uploadDTO.getFilePath();
      String[] parts = path.split("/");
      String queryPath = "";
      Long upperParentId = null;
      for (String part : parts) {
        if (part.equals("")) {
          continue;
        }
        // 查询是否存在对应path的文件夹记录
        queryPath += part + "/";
        LambdaQueryWrapper<File> queryWrapper = new LambdaQueryWrapper<File>().eq(File::getFilepath, queryPath).eq(File::getType, 1).eq(File::getStatus, 0);
        File dbPath = this.getOne(queryWrapper);
        if (dbPath == null) {
          // 不存在则创建
          File savePath = File.builder()
              .userId(loginId)
              .filename(part)
              .filepath(queryPath)
              .parentId(upperParentId)
              .storageType(1) // 默认1即为阿里云OSS，0为本地存储
              .type(1) // 是文件夹
              .status(0)
              .build();
          this.save(savePath);
          upperParentId = savePath.getId();
        } else {
          // 存在该目录，直接使用其作为父级
          upperParentId = dbPath.getId();
        }
      }
      // 如果新创建了目录，赋值为新的目录ID
      if (upperParentId != null) parentId = upperParentId;
      File file = File.builder()
          .userId(loginId)
          .filename(fileName)
          .filepath(uploadDTO.getFilePath())
          .md5(fileMD5)
          .parentId(parentId)
          .size(uploadDTO.getFile().getSize())
          .storageType(1) // 默认1即为阿里云OSS，0为本地存储
          .suffix(fileSuffix)
          .type(0) // 是文件
          .status(0)
          .build();
      this.save(file);
      FileUploadProxy fileUploadProxy = fileUploadFactory.newInstance();
      // 使用MD5作为OSS文件名
      fileUploadProxy.uploadForDrive(fileMD5, uploadDTO.getFile());
    } catch (Exception e) {
      e.printStackTrace();
      throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传文件失败");
    }
    return true;
  }

  /**
   * 列出文件列表，分页查询，根据目录id逐级查询
   *
   * @param page
   * @param dirId
   * @return
   */
  @Override
  public IPage<FileVO> listFiles(IPage<FileVO> page, Long dirId) {
    if (dirId == null) {
      // 查询根目录，查询所有parentId=null的文件
      LambdaQueryWrapper<File> queryWrapper = new LambdaQueryWrapper<File>().eq(File::getParentId, null);
      this.page(page, queryWrapper);
    }
    return null;
  }
}




