package cn.pepedd.drive.controller.api;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.hutool.core.util.IdUtil;
import cn.pepedd.drive.common.cache.RedisCache;
import cn.pepedd.drive.common.constants.CacheConstants;
import cn.pepedd.drive.common.constants.Constants;
import cn.pepedd.drive.common.enums.ErrorCode;
import cn.pepedd.drive.common.satoken.StpKit;
import cn.pepedd.drive.common.satoken.StpType;
import cn.pepedd.drive.entity.dto.LoginBodyDTO;
import cn.pepedd.drive.entity.dto.RegisterBodyDTO;
import cn.pepedd.drive.entity.dto.UserUpdateDTO;
import cn.pepedd.drive.entity.result.R;
import cn.pepedd.drive.entity.vo.UserVO;
import cn.pepedd.drive.service.UserService;
import com.google.code.kaptcha.Producer;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static cn.pepedd.drive.common.constants.Constants.CAPTCHA_ENABLED;

/**
 * 用户接口
 *
 * @author pepedd864
 * @since 2023/12/4
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
  @Resource
  private UserService userService;
  @Resource(name = "captchaProducer")
  private Producer captchaProducer;

  @Resource(name = "captchaProducerMath")
  private Producer captchaProducerMath;

  @Resource
  private RedisCache redisCache;


  /**
   * 登录
   *
   * @param loginBodyDto 登录信息
   * @return 登录结果
   */
  @PostMapping("/login")
  @ApiOperation("登录")
  public R<String> login(@RequestBody @Valid LoginBodyDTO loginBodyDto) {
    return userService.login(loginBodyDto);
  }

  /**
   * 登出
   *
   * @return
   */
  @GetMapping("/logout")
  @ApiOperation("登出")
  public R<Boolean> logout() {
    return userService.logout();
  }

  /**
   * 是否登录
   *
   * @return
   */
  @SaCheckLogin(type = StpType.USER)
  @GetMapping("isLogin")
  @ApiOperation("是否登录")
  public R<Boolean> isLogin() {
    return R.success(StpKit.USER.isLogin());
  }

  /**
   * 生成验证码
   *
   * @return 验证码
   */
  @GetMapping("/captchaImage")
  @ApiOperation("验证码")
  public R<Map<String, Object>> captchaImage() {
    Map<String, Object> captcha = new HashMap<>();

    // 读取配置文件是否开启验证码，这里默认开启
    // boolean captchaEnabled = configService.selectCaptchaEnabled();
    boolean captchaEnabled = CAPTCHA_ENABLED;
    captcha.put("captchaEnabled", captchaEnabled);
    if (!captchaEnabled) {
      return R.success(captcha);
    }

    // 生成唯一id存入redis，后续校验可以取出使用
    String uuid = IdUtil.simpleUUID();
    String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;

    String capStr = null, code = null;
    BufferedImage image = null;

    // 生成验证码 默认是计算类型的验证码 可以为传统的字符模式
    String captchaType = "math";
    if ("math".equals(captchaType)) {
      String capText = captchaProducerMath.createText();
      capStr = capText.substring(0, capText.lastIndexOf("@"));
      code = capText.substring(capText.lastIndexOf("@") + 1);
      image = captchaProducerMath.createImage(capStr);
    } else if ("char".equals(captchaType)) {
      capStr = code = captchaProducer.createText();
      image = captchaProducer.createImage(capStr);
    }

    redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES); // 过期时间2分钟
    // 转换流信息写出
    FastByteArrayOutputStream os = new FastByteArrayOutputStream();
    try {
      ImageIO.write(image, "jpg", os);
    } catch (IOException e) {
      return R.error(ErrorCode.SYSTEM_ERROR, e.getMessage());
    }

    captcha.put("uuid", uuid);
    captcha.put("img", os.toByteArray());
    return R.success(captcha);
  }

  /**
   * 注册
   *
   * @param registerBodyDTO 注册信息
   * @return 登录消息
   */
  @PostMapping("/register")
  @ApiOperation("注册")
  public R<String> register(@RequestBody @Valid RegisterBodyDTO registerBodyDTO) {
    return userService.register(registerBodyDTO);
  }

  /**
   * 获取用户信息
   *
   * @return
   */
  @SaCheckLogin(type = StpType.USER)
  @GetMapping("/getInfo")
  @ApiOperation("获取用户信息")
  public R<UserVO> getInfo() {
    return userService.getInfo();
  }

  /**
   * 更新用户信息
   *
   * @param userUpdateDTO
   * @return
   */
  @SaCheckLogin(type = StpType.USER)
  @PostMapping("/updateInfo")
  @ApiOperation("更新用户信息")
  public R<Boolean> updateInfo(@RequestBody @Valid UserUpdateDTO userUpdateDTO) {
    return userService.updateInfo(userUpdateDTO);
  }

  @GetMapping("/login/test")
  @ApiOperation("用户测试登录")
  public R testLogin() {
    // Token挂载的扩展参数 （此方法只有在集成jwt插件时才会生效）
    SaLoginModel loginModel = new SaLoginModel();
    loginModel.setExtra("username", "test user");
    StpKit.USER.login(1, loginModel);

    SaTokenInfo saTokenInfo = StpKit.USER.getTokenInfo();
    Map<String, Object> map = new HashMap<>();
    map.put("token", saTokenInfo.tokenValue);
    return R.success(map);
  }

  @SaCheckLogin(type = StpType.USER)
  @GetMapping("/info/test")
  @ApiOperation("用户测试获取信息")
  public R testInfo() {
    // 支持从jwt中取出信息
    return R.success(StpKit.USER.getExtra("username"));
  }
}
