package cn.pepedd.drive.service.impl;

import cn.pepedd.drive.common.enums.ErrorCode;
import cn.pepedd.drive.common.upload.FileUploadFactory;
import cn.pepedd.drive.common.upload.FileUploadProxy;
import cn.pepedd.drive.common.utils.FileUtils;
import cn.pepedd.drive.entity.dto.SingleFileUploadDTO;
import cn.pepedd.drive.entity.pojo.File;
import cn.pepedd.drive.exception.BusinessException;
import cn.pepedd.drive.exception.ThrowUtils;
import cn.pepedd.drive.mapper.FileMapper;
import cn.pepedd.drive.service.FileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @description 针对表【tb_file(文件信息表)】的数据库操作Service实现
 * @createDate 2024-08-02 14:23:26
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File>
    implements FileService {
  @Autowired(required = false)
  private FileUploadFactory fileUploadFactory;

  /**
   * 小文件上传
   *
   * @param uploadDTO
   * @return
   */
  @Override
  public Boolean singleFileUpload(SingleFileUploadDTO uploadDTO) {
    // 校验MD5是否正确
    try {
      String realMd5 = FileUtils.calculateFileMD5(uploadDTO.getFile().getInputStream());
      ThrowUtils.throwIf(!realMd5.equals(uploadDTO.getFileMd5()), ErrorCode.PARAMS_ERROR, "MD5校验失败");
      FileUploadProxy fileUploadProxy = fileUploadFactory.newInstance();
      // 使用MD5作为OSS文件名
      fileUploadProxy.uploadForDrive(uploadDTO.getFileMd5(), uploadDTO.getFile());
    } catch (Exception e) {
      throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传文件失败");
    }
    return true;
  }
}




