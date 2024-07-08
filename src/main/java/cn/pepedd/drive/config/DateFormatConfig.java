package cn.pepedd.drive.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * 时间格式化配置
 */
@JsonComponent
public class DateFormatConfig {
  @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
  private String pattern;
  @Resource
  @Lazy
  private LocalDateTimeSerializer localDateTimeDeserializer;

  /**
   * date 类型全局时间格式化
   */
  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilder() {

    return builder -> {
      TimeZone tz = TimeZone.getTimeZone("UTC");
      DateFormat df = new SimpleDateFormat(pattern);
      df.setTimeZone(tz);
      builder.failOnEmptyBeans(false)
          .failOnUnknownProperties(false)
          .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
          .dateFormat(df);
    };
  }

  /**
   * LocalDate 类型全局时间格式化
   */
  @Bean
  public LocalDateTimeSerializer localDateTimeDeserializer() {
    return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern));
  }

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
    return builder -> builder.serializerByType(LocalDateTime.class, localDateTimeDeserializer);
  }
}
