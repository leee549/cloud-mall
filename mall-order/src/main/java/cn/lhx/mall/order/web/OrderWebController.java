package cn.lhx.mall.order.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author lee549
 * @date 2020/12/4 15:46
 */
@Controller
public class OrderWebController {

    @GetMapping("/toTrade")
    public String toTrade(){
        return "confirm";
    }
}
