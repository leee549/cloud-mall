package cn.lhx.mall.thirdpart.controller;

import cn.lhx.common.utils.R;
import cn.lhx.mall.thirdpart.component.SmsComponent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lee549
 * @date 2020/11/14 17:51
 */
@RestController
@RequestMapping("/sms")
public class SmsSendController {

    @Resource
    private SmsComponent smsComponent;
    @GetMapping("/sendcode")
    public R SendCode(@RequestParam("phone") String phone,@RequestParam("code") String code ){
        smsComponent.sendSmsCode(phone,code);
        return R.ok();
    }
}
