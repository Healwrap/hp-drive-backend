package cn.pepedd.drive.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.jwt.SaJwtTemplate;
import cn.dev33.satoken.jwt.SaJwtUtil;
import cn.hutool.jwt.JWT;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {
  // 注册 Sa-Token 拦截器，打开注解式鉴权功能
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // 注册 Sa-Token 拦截器，定义详细认证规则

    // --------------------- // 注解式鉴权 // --------------------- //
    registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
  }

//  /**
//   * 自定义 SaJwtUtil 生成 token 的算法 TODO 目前不需要自定义
//   */
//  @PostConstruct
//  public void setSaJwtTemplate() {
//    SaJwtUtil.setSaJwtTemplate(new SaJwtTemplate() {
//      @Override
//      public String generateToken(JWT jwt, String keyt) {
//        System.out.println("------ 自定义了 token 生成算法");
//        return super.generateToken(jwt, keyt);
//      }
//    });
//  }

}
