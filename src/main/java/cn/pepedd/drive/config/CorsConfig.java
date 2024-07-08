package cn.pepedd.drive.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 解决跨域问题 注：CORS是一种解决请求跨域的方案 通过在响应头添加"Access-Control-Allow-Origin"指定访问资源
 *
 * @author pepedd864
 * @since 2023/12/6
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**") // 所有接口
        .allowCredentials(true) // 是否发送 Cookie
        .allowedOriginPatterns("*") // 支持域
        .allowedMethods(new String[]{"GET", "POST", "PUT", "DELETE"}) // 支持方法
        .allowedHeaders("*")
        .exposedHeaders("*");
  }
}
