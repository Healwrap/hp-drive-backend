package cn.pepedd.drive.controller.view;

import cn.pepedd.drive.common.enums.ErrorCode;
import cn.pepedd.drive.entity.result.R;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 覆盖SpringBoot 默认错误处理器
 */
@Controller
@ApiIgnore
public class ErrorController extends BasicErrorController {
  public ErrorController(ErrorAttributes errorAttributes,
                         ServerProperties serverProperties,
                         List<ErrorViewResolver> errorViewResolvers) {
    super(errorAttributes, serverProperties.getError(), errorViewResolvers);
  }

  /**
   * 返回的html/text页面
   *
   * @param request
   * @param response
   * @return
   */
  @Override
  public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
    HttpStatus status = getStatus(request);
    // 获取 Spring Boot 默认提供的错误信息，然后添加一个自定义的错误信息
    Map<String, Object> model = getErrorAttributes(request,
        getErrorAttributeOptions(request, MediaType.TEXT_HTML));
    return new ModelAndView("errorPage", model, status);
  }

  /**
   * 返回的json数据
   *
   * @param request
   * @return
   */
  @Override
  public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
    HttpStatus status = getStatus(request);
    // 获取 Spring Boot 默认提供的错误信息，然后添加一个自定义的错误信息
    Map<String, Object> body = getErrorAttributes(request,
        getErrorAttributeOptions(request, MediaType.TEXT_HTML));
    return new ResponseEntity<>(R.error(ErrorCode.SYSTEM_ERROR).toMap(), status);
  }
}
