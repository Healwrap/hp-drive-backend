package cn.pepedd.drive.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.pepedd.drive.entity.pojo.Setting;
import cn.pepedd.drive.service.SettingService;
import cn.pepedd.drive.mapper.SettingMapper;
import org.springframework.stereotype.Service;

/**
* @author admin
* @description 针对表【sys_setting(设置表)】的数据库操作Service实现
* @createDate 2024-08-02 14:23:26
*/
@Service
public class SettingServiceImpl extends ServiceImpl<SettingMapper, Setting>
    implements SettingService{

}




