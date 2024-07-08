package cn.pepedd.drive.service.impl;

import cn.pepedd.drive.entity.dto.LoginBodyDTO;
import cn.pepedd.drive.entity.result.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.pepedd.drive.entity.pojo.Admin;
import cn.pepedd.drive.service.AdminService;
import cn.pepedd.drive.mapper.AdminMapper;
import org.springframework.stereotype.Service;

/**
* @author admin
* @description 针对表【sys_admin(管理员表)】的数据库操作Service实现
* @createDate 2024-08-02 14:23:26
*/
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>
    implements AdminService{

  @Override
  public R<Boolean> login(LoginBodyDTO loginBodyDto) {
    return null;
  }
}




