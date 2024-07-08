package cn.pepedd.drive.controller.api;

import cn.pepedd.drive.entity.dto.SingleFileUploadDTO;
import cn.pepedd.drive.entity.result.R;
import cn.pepedd.drive.service.FileService;
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

  @PostMapping("/single/upload")
  public R<Boolean> singleFileUpload(@Valid SingleFileUploadDTO uploadDTO) {
    return R.success(fileService.singleFileUpload(uploadDTO));
  }
}
