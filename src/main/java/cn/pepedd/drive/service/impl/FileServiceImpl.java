package cn.pepedd.drive.service.impl;

import cn.pepedd.drive.common.enums.ErrorCode;
import cn.pepedd.drive.common.enums.FileStatusEnum;
import cn.pepedd.drive.common.satoken.StpKit;
import cn.pepedd.drive.common.upload.FileUploadFactory;
import cn.pepedd.drive.common.upload.FileUploadProxy;
import cn.pepedd.drive.common.utils.FileUtils;
import cn.pepedd.drive.entity.dto.singlefile.SingleFileHandShakeDTO;
import cn.pepedd.drive.entity.dto.singlefile.SingleFileUploadDTO;
import cn.pepedd.drive.entity.page.FileListDTO;
import cn.pepedd.drive.entity.pojo.File;
import cn.pepedd.drive.entity.vo.FileVO;
import cn.pepedd.drive.exception.BusinessException;
import cn.pepedd.drive.mapper.FileMapper;
import cn.pepedd.drive.service.FileService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
   * 根据目录生成
   *
   * @param path
   * @param loginId
   * @return
   */
  private Long generateDirectory(String path, Long loginId) {
    // 首先根据filepath生成对应的数据库记录
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
    return upperParentId;
  }

  /**
   * 小文件上传握手
   * 握手，主要是确定文件是否已上传 并创建文件夹记录
   *
   * @param fileHandShakeDTO
   * @return
   */
  @Override
  public Long singleFileHandShake(SingleFileHandShakeDTO fileHandShakeDTO) {
    // 如果握手过，但是因为意外中断上传了，返回上传ID
    LambdaQueryWrapper<File> queryWrapper = new LambdaQueryWrapper<File>().eq(File::getStatus, 0);
//    this.getOne()

    // 处理参数
    Long parentId = fileHandShakeDTO.getParentId();
    Long loginId = Long.parseLong((String) StpKit.USER.getLoginId());
    // 写入数据库
    // 首先根据filepath生成对应的数据库记录
    String path = fileHandShakeDTO.getFilePath();
    // 生成目录
    Long upperParentId = generateDirectory(path, loginId);
    // 如果新创建了目录，赋值为新的目录ID
    if (upperParentId != null) parentId = upperParentId;
    File file = File.builder()
        .userId(loginId)
        .filepath(fileHandShakeDTO.getFilePath())
        .md5(fileHandShakeDTO.getMd5())
        .parentId(parentId)
        .storageType(1) // 默认1即为阿里云OSS，0为本地存储
        .type(0) // 是文件
        .status(0)
        .build();
    this.save(file);


    return file.getId();
  }

  /**
   * 小文件上传，根据握手返回的uploadId更新记录并上传文件
   *
   * @param uploadDTO
   * @return
   */
  @Override
  public Boolean singleFileUpload(SingleFileUploadDTO uploadDTO) {
    try {
      Long loginId = Long.parseLong((String) StpKit.USER.getLoginId());
      // 上传ID
      Long uploadId = uploadDTO.getUploadId();
      // 计算MD5
      String fileMD5 = FileUtils.calculateFileMD5(uploadDTO.getFile().getInputStream());
      // 该用户下为上传文件
      LambdaQueryWrapper<File> queryWrapper = new LambdaQueryWrapper<File>().eq(File::getUserId, loginId).eq(File::getStatus, FileStatusEnum.NOT_UPLOADED.getCode());
      // 对比md5是否是正确的文件
      File dbFile = this.getOne(queryWrapper.eq(File::getId, uploadId));
      if (dbFile == null || !dbFile.getMd5().equals(fileMD5)) {
        throw new BusinessException(ErrorCode.PARAMS_ERROR);
      }
      // 上传文件
      FileUploadProxy fileUploadProxy = fileUploadFactory.newInstance();
      // 使用MD5作为OSS文件名
      fileUploadProxy.uploadForDrive(fileMD5, uploadDTO.getFile());
      // 写入数据库
      // 处理参数
      String fileName = uploadDTO.getFile().getOriginalFilename();
      String fileSuffix = FileUtils.getFileExtension(fileName);
      dbFile.setSize(uploadDTO.getFile().getSize());
      dbFile.setFilename(fileName);
      dbFile.setSuffix(fileSuffix);
      dbFile.setStatus(FileStatusEnum.UPLOADED.getCode());
      this.save(dbFile);
    } catch (Exception e) {
      e.printStackTrace();
      throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传文件失败");
    }
    return true;
  }

  /**
   * 列出文件列表，分页查询，根据目录id逐级查询
   *
   * @param fileListDTO
   * @return
   */
  @Override
  public IPage<FileVO> listFiles(FileListDTO fileListDTO) {
    Long userId = Long.parseLong((String) StpKit.USER.getLoginId());
    Page page = fileListDTO.getPageQuery();
    // 起始查询条件
    LambdaQueryWrapper<File> queryWrapper = new LambdaQueryWrapper<File>().eq(File::getUserId, userId).eq(File::getStatus, FileStatusEnum.UPLOADED.getCode());
    if (fileListDTO.getDirId() == null) {
      // 查询根目录，查询所有parentId=null的文件
      queryWrapper.isNull(File::getParentId);
    } else {
      // 如果指定了 dirId，查询该目录下的文件
      queryWrapper.eq(File::getParentId, fileListDTO.getDirId());
    }
    IPage<File> selectPage = this.baseMapper.selectPage(page, queryWrapper);
    List<File> records = selectPage.getRecords();
    List<FileVO> fileVOS = new ArrayList<>();
    for (File record : records) {
      FileVO fileVO = new FileVO();
      BeanUtils.copyProperties(record, fileVO);
      fileVOS.add(fileVO);
    }
    page.setRecords(fileVOS);
    return page;
  }
}




