package cn.lhx.mall.auth.controller;

import cn.lhx.common.constant.AuthServerConstant;
import cn.lhx.common.exception.BizCodeEnum;
import cn.lhx.common.utils.R;
import cn.lhx.mall.auth.feign.ThirdPartFeignService;
import cn.lhx.mall.auth.vo.UserRegVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author lee549
 * @date 2020/11/13 16:50
 */
@Controller
public class AuthController {

    @Resource
    private ThirdPartFeignService thirdPartFeignService;

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;


    @GetMapping("/sms/sendcode")
    @ResponseBody
    public R sendCode(@RequestParam("phone") String phone) {
        //1接口防刷

        //2Redis校验验证码   sms:code:1231313  ->  213456
        String redisCode = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
        if (StringUtils.isNotEmpty(redisCode) ) {
            long l = Long.parseLong(redisCode.split("_")[1]);
            if (System.currentTimeMillis() - l < 60000){
                //60s不能再发
                return R.error(BizCodeEnum.SMS_CODE_EXCEPTION.code, BizCodeEnum.SMS_CODE_EXCEPTION.msg);
            }
        }
        String code = randomCode();
        String setCode=code+"_"+System.currentTimeMillis();
        //10分钟有效
        redisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone, setCode, 10, TimeUnit.MINUTES);
        thirdPartFeignService.SendCode(phone, code);
        return R.ok();
    }


    @PostMapping("/regist")
    public String regist(@Valid UserRegVo vo, BindingResult result, Model model){
        if (result.hasErrors()){
            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            model.addAttribute("errors",errors);
            return "forward:/register.html";
        }

        //注册

        return "redirect:/login.html";
    }

    /**
     * 生成6位数字
     *
     * @return
     */
    public static String randomCode() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }
}
