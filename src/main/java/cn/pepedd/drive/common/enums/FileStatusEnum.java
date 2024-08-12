package cn.pepedd.drive.common.enums;

/**
 * 文件状态枚举
 *
 * @author pepedd864
 * @since 2024/8/6
 */
public enum FileStatusEnum {
  NOT_UPLOADED(0, "未上传"),
  UPLOADING(1, "上传中"),
  UPLOADED(2, "已上传"),
  DELETED(3, "放入回收站");

  private final int code;
  private final String message;

  FileStatusEnum(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}
