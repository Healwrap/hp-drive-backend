package cn.pepedd.drive.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 主页面控制类
 *
 * @author pepedd864
 * @since 2024/6/6
 */
@Controller
@RequestMapping("/")
@ApiIgnore
public class IndexController {
  @RequestMapping("/")
  public String index() {
    return "index";
  }

  @RequestMapping("/errorPage")
  public String error() {
    return "errorPage";
  }
}
