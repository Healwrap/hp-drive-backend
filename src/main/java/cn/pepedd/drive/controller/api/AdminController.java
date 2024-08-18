package cn.pepedd.drive.controller.api;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.pepedd.drive.common.satoken.StpKit;
import cn.pepedd.drive.common.satoken.StpType;
import cn.pepedd.drive.entity.dto.userAuth.LoginBodyDTO;
import cn.pepedd.drive.entity.result.R;
import cn.pepedd.drive.service.AdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理员接口
 *
 * @author pepedd864
 * @since 2023/11/24
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {
  @Resource
  private AdminService adminService;

  /**
   * 登录
   *
   * @param loginBodyDto 登录信息
   * @return 登录结果
   */
  @PostMapping("/login")
  @ApiOperation("登录")
  public R<Boolean> login(@RequestBody @Valid LoginBodyDTO loginBodyDto) {
    return adminService.login(loginBodyDto);
  }

  @GetMapping("/login/test")
  @ApiOperation("管理员测试登录")
  public R testLogin() {
    // Token挂载的扩展参数 （此方法只有在集成jwt插件时才会生效）
    SaLoginModel loginModel = new SaLoginModel();
    loginModel.setExtra("username", "test admin");
    StpKit.ADMIN.login(1, loginModel);

    SaTokenInfo saTokenInfo = StpKit.ADMIN.getTokenInfo();
    Map<String, Object> map = new HashMap<>();
    map.put("token", saTokenInfo.tokenValue);
    return R.success(map);
  }

  @SaCheckLogin(type = StpType.ADMIN)
  @GetMapping("/info/test")
  @ApiOperation("管理员测试获取信息")
  public R testInfo() {
    return R.success(StpKit.ADMIN.getExtra("username"));
  }
}
