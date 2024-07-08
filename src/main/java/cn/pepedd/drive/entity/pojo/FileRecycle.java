package cn.pepedd.drive.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 文件回收站
 * @TableName tb_file_recycle
 */
@TableName(value ="tb_file_recycle")
@Data
public class FileRecycle implements Serializable {
    /**
     *
     */
    @TableId
    private Long id;

    /**
     * 文件ID
     */
    private Long fileId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
