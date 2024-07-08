package cn.pepedd.drive.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlus配置类
 */
@Configuration
public class MybatisPlusConfig
{
  @Bean
  public MybatisPlusInterceptor mybatisPlusInterceptor()
  {
    MybatisPlusInterceptor mpi = new MybatisPlusInterceptor();
    mpi.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
    return mpi;
  }
}
