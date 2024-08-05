package cn.pepedd.drive.common.utils;

import java.io.InputStream;
import java.security.MessageDigest;

/**
 * TODO
 *
 * @author pepedd864
 * @since 2024/8/2
 */
public class FileUtils {
  /**
   * 计算文件的MD5值
   *
   * @param is
   * @return
   * @throws Exception
   */
  public static String calculateFileMD5(InputStream is) throws Exception {
    MessageDigest digest = MessageDigest.getInstance("MD5");
    byte[] buffer = new byte[8192];
    int read;
    while ((read = is.read(buffer)) > 0) {
      digest.update(buffer, 0, read);
    }
    byte[] hash = digest.digest();
    StringBuilder hexString = new StringBuilder();
    for (byte b : hash) {
      String hex = Integer.toHexString(0xff & b);
      if (hex.length() == 1) hexString.append('0');
      hexString.append(hex);
    }
    return hexString.toString();
  }

  public static String getFileExtension(String fileName) {
    int dotIndex = fileName.lastIndexOf(".");
    if (dotIndex == -1) {
      return "";
    }
    return fileName.substring(dotIndex + 1);
  }
}
