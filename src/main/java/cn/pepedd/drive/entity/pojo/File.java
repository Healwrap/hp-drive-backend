package cn.pepedd.drive.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 文件信息表
 * @TableName tb_file
 */
@TableName(value ="tb_file")
@Data
public class File implements Serializable {
    /**
     *
     */
    @TableId
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 父级ID
     */
    private Long parentId;

    /**
     * 文件名
     */
    private String filename;

    /**
     * 文件路径
     */
    private String filepath;

    /**
     * 类型（0是文件、1是文件夹）
     */
    private Integer type;

    /**
     * 文件后缀
     */
    private String suffix;

    /**
     * 文件的唯一编码，用于识别文件
     */
    private Integer md5;

    /**
     * 存储类型（使用本地存储或者OSS服务，待定）
     */
    private Integer storageType;

    /**
     * 占用大小（字节）
     */
    private Long size;

    /**
     * 状态（正常0、放入回收站1）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
