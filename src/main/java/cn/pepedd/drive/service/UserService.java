package cn.pepedd.drive.service;

import cn.pepedd.drive.entity.dto.LoginBodyDTO;
import cn.pepedd.drive.entity.dto.RegisterBodyDTO;
import cn.pepedd.drive.entity.dto.UserUpdateDTO;
import cn.pepedd.drive.entity.pojo.User;
import cn.pepedd.drive.entity.result.R;
import cn.pepedd.drive.entity.vo.UserVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author admin
* @description 针对表【tb_user(用户表)】的数据库操作Service
* @createDate 2024-08-02 14:23:26
*/
public interface UserService extends IService<User> {
  /**
   * 登录
   *
   * @param loginBodyDto
   * @return
   */
  R<String> login(LoginBodyDTO loginBodyDto);

  /**
   * 登出
   *
   * @return
   */
  R<Boolean> logout();
  /**
   * 注册
   *
   * @param registerBodyDTO
   * @return
   */
  R<String> register(RegisterBodyDTO registerBodyDTO);

  /**
   * 更新用户信息
   * @param userUpdateDTO
   * @return
   */
  R<Boolean> updateInfo(UserUpdateDTO userUpdateDTO);

  /**
   * 获取用户信息
   *
   * @return
   */
  R<UserVO> getInfo();
}
