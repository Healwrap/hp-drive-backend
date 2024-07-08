package cn.pepedd.drive.common.satoken;

import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;

/**
 * 多账号认证
 *
 * @author pepedd864
 * @since 2024/8/2
 */
public class StpKit {
  /**
   * 默认原生会话对象
   */
  public static final StpLogic DEFAULT = StpUtil.stpLogic;

  /**
   * Admin 会话对象，管理 Admin 表所有账号的登录、权限认证
   */
  public static final StpLogic ADMIN = new StpLogicJwtForSimple(StpType.ADMIN);

  /**
   * User 会话对象，管理 User 表所有账号的登录、权限认证
   */
  public static final StpLogic USER = new StpLogicJwtForSimple(StpType.USER);

}
