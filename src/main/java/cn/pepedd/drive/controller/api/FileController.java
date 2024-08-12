package cn.pepedd.drive.controller.api;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.pepedd.drive.common.satoken.StpType;
import cn.pepedd.drive.entity.dto.MultipartHandshakeDTO;
import cn.pepedd.drive.entity.dto.SingleFileUploadDTO;
import cn.pepedd.drive.entity.result.R;
import cn.pepedd.drive.service.FileService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
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

  @SaCheckLogin(type = StpType.USER)
  @PostMapping("/single/handshake")
  @ApiOperation("小文件上传握手")
  public R<Boolean> singleFileHandleShake(@Valid SingleFileUploadDTO uploadDTO) {
    return R.success(true);
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
}
