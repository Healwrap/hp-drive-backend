package cn.pepedd.drive.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 设置表
 * @TableName sys_setting
 */
@TableName(value ="sys_setting")
@Data
public class Setting implements Serializable {
    /**
     *
     */
    @TableId
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 空间大小（字节，默认100M）
     */
    private Long spaceSize;

    /**
     * 上传速度（字节，默认为-1即不限速）
     */
    private Long uploadSpeed;

    /**
     * 下载速度（字节，默认为-1即不限速）
     */
    private Long downloadSpeed;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
