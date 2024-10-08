package cn.pepedd.drive.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自动填充字段
 *
 * @author pepedd864
 * @since 2023/10/2 20:41
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
  @Override
  public void insertFill(MetaObject metaObject) {
    log.info("start insert fill ...");
    strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
    strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
  }

  @Override
  public void updateFill(MetaObject metaObject) {
    log.info("start update fill ...");
    strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
  }
}
