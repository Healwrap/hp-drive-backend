package cn.pepedd.drive.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * TODO
 *
 * @author pepedd864
 * @since 2024/8/17
 */
@Data
@Builder
public class FileVO implements Serializable {
  /**
   *
   */
  @TableId
  private Long id;

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
   * 占用大小（字节）
   */
  private Long size;

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
}
