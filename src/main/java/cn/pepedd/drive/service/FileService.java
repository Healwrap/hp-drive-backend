package cn.pepedd.drive.service;

import cn.pepedd.drive.entity.dto.SingleFileUploadDTO;
import cn.pepedd.drive.entity.pojo.File;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author admin
* @description 针对表【tb_file(文件信息表)】的数据库操作Service
* @createDate 2024-08-02 14:23:26
*/
public interface FileService extends IService<File> {
  /**
   * 小文件上传
   * @param uploadDTO
   * @return
   */
  Boolean singleFileUpload(SingleFileUploadDTO uploadDTO);
}
