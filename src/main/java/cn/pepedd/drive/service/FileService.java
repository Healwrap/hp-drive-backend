package cn.pepedd.drive.service;

import cn.pepedd.drive.entity.dto.singlefile.SingleFileHandShakeDTO;
import cn.pepedd.drive.entity.dto.singlefile.SingleFileUploadDTO;
import cn.pepedd.drive.entity.page.FileListDTO;
import cn.pepedd.drive.entity.pojo.File;
import cn.pepedd.drive.entity.vo.FileVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author admin
 * @description 针对表【tb_file(文件信息表)】的数据库操作Service
 * @createDate 2024-08-02 14:23:26
 */
public interface FileService extends IService<File> {
  /**
   * 小文件上传握手
   * 握手，主要是确定文件是否已上传 并创建文件夹记录
   *
   * @param fileHandShakeDTO
   * @return
   */
  Long singleFileHandShake(SingleFileHandShakeDTO fileHandShakeDTO);

  /**
   * 小文件上传
   *
   * @param uploadDTO
   * @return
   */
  Boolean singleFileUpload(SingleFileUploadDTO uploadDTO);

  /**
   * 列出文件列表，分页查询，根据目录id逐级查询
   *
   * @param fileListDTO
   * @return
   */
  IPage<FileVO> listFiles(FileListDTO fileListDTO);
}
