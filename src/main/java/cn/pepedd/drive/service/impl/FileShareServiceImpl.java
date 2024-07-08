package cn.pepedd.drive.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.pepedd.drive.entity.pojo.FileShare;
import cn.pepedd.drive.service.FileShareService;
import cn.pepedd.drive.mapper.FileShareMapper;
import org.springframework.stereotype.Service;

/**
* @author admin
* @description 针对表【tb_file_share(文件分享表)】的数据库操作Service实现
* @createDate 2024-08-02 14:23:26
*/
@Service
public class FileShareServiceImpl extends ServiceImpl<FileShareMapper, FileShare>
    implements FileShareService{

}




