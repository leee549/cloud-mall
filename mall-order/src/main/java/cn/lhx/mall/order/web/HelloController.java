package cn.lhx.mall.order.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author lee549
 * @date 2020/12/4 9:54
 */
@Controller
public class HelloController {

    @RequestMapping("/{page}.html")
    public String hello(@PathVariable("page")String page){
        return page;
    }
}
