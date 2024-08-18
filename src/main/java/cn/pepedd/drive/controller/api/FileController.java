package cn.pepedd.drive.controller.api;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.pepedd.drive.common.satoken.StpType;
import cn.pepedd.drive.entity.dto.multipartfile.MultipartHandshakeDTO;
import cn.pepedd.drive.entity.dto.singlefile.SingleFileHandShakeDTO;
import cn.pepedd.drive.entity.dto.singlefile.SingleFileUploadDTO;
import cn.pepedd.drive.entity.page.FileListDTO;
import cn.pepedd.drive.entity.result.R;
import cn.pepedd.drive.entity.vo.FileVO;
import cn.pepedd.drive.service.FileService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 文件接口
 *
 * @author pepedd864
 * @since 2024/8/3
 */
@RestController
@RequestMapping("/api/file")
public class FileController {
  @Resource
  private FileService fileService;

  /**
   * 小文件上传握手
   *
   * @param fileHandShakeDTO
   * @return
   */
  @SaCheckLogin(type = StpType.USER)
  @PostMapping("/single/handshake")
  @ApiOperation("小文件上传握手")
  public R<Long> singleFileHandShake(@Valid SingleFileHandShakeDTO fileHandShakeDTO) {
    return R.success(fileService.singleFileHandShake(fileHandShakeDTO));
  }

  /**
   * 小文件上传
   *
   * @param uploadDTO
   * @return
   */
  @SaCheckLogin(type = StpType.USER)
  @PostMapping("/single/upload")
  @ApiOperation("小文件上传")
  public R<Boolean> singleFileUpload(@Valid SingleFileUploadDTO uploadDTO) {
    return R.success(fileService.singleFileUpload(uploadDTO));
  }

  /**
   * 大文件上传握手
   *
   * @param handshakeDTO
   * @return
   */
  @SaCheckLogin(type = StpType.USER)
  @PostMapping("/multipart/handshake")
  @ApiOperation("大文件上传握手")
  public R multipartHandshake(@Valid MultipartHandshakeDTO handshakeDTO) {
    return R.success();
  }

  /**
   * 获取文件列表
   *
   * @param fileListDTO
   * @return
   */
  @SaCheckLogin(type = StpType.USER)
  @PostMapping("/list")
  @ApiOperation("文件列表")
  public R<IPage<FileVO>> list(@RequestBody FileListDTO fileListDTO) {
    return R.success(fileService.listFiles(fileListDTO));
  }
}
