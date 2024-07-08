package cn.pepedd.drive;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot启动类
 *
 * @author pepedd864
 * @since 2023/11/24
 */
@SpringBootApplication
@MapperScan("cn.pepedd.drive.mapper")
public class Main {
  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }
}
