package cn.pepedd.drive.service;

import cn.pepedd.drive.entity.dto.userAuth.LoginBodyDTO;
import cn.pepedd.drive.entity.pojo.Admin;
import cn.pepedd.drive.entity.result.R;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author admin
* @description 针对表【sys_admin(管理员表)】的数据库操作Service
* @createDate 2024-08-02 14:23:26
*/
public interface AdminService extends IService<Admin> {

  R<Boolean> login(LoginBodyDTO loginBodyDto);
}
