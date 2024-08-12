package cn.pepedd.drive.entity.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件信息表
 *
 * @TableName tb_file
 */
@TableName(value = "tb_file")
@Data
@Builder
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
   * 描述（使用AI识别生成简介）
   */
  private String description;

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
  private String md5;

  /**
   * 存储类型（使用本地存储或者OSS服务，待定）
   */
  private Integer storageType;

  /**
   * 占用大小（字节）
   */
  private Long size;

  /**
   * 状态（待上传0、上传中1、已上传2、放入回收站3）
   */
  private Integer status;

  /**
   * 创建时间
   */
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;

  /**
   * 修改时间
   */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Date updateTime;

  @TableField(exist = false)
  private static final long serialVersionUID = 1L;
}
