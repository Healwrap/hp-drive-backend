package cn.pepedd.drive.entity.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询文件列表参数
 *
 * @author pepedd864
 * @since 2024/8/18
 */
@Data
public class FileListDTO implements Serializable {
  Long dirId;
  Page pageQuery;
}
