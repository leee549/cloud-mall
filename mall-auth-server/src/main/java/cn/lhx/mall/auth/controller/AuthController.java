package cn.lhx.mall.auth.controller;

import cn.lhx.common.constant.AuthServerConstant;
import cn.lhx.common.exception.BizCodeEnum;
import cn.lhx.common.utils.R;
import cn.lhx.mall.auth.feign.MemberFeignService;
import cn.lhx.mall.auth.feign.ThirdPartFeignService;
import cn.lhx.mall.auth.vo.UserLoginVo;
import cn.lhx.mall.auth.vo.UserRegVo;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
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

    @Resource
    private MemberFeignService memberFeignService;


    @GetMapping("/sms/sendcode")
    @ResponseBody
    public R sendCode(@RequestParam("phone") String phone) {
        //1接口防刷

        //2Redis校验验证码   sms:code:1231313  ->  213456
        String redisCode = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
        if (StringUtils.isNotEmpty(redisCode)) {
            long l = Long.parseLong(redisCode.split("_")[1]);
            if (System.currentTimeMillis() - l < 60000) {
                //60s不能再发
                return R.error(BizCodeEnum.SMS_CODE_EXCEPTION.code, BizCodeEnum.SMS_CODE_EXCEPTION.msg);
            }
        }
        String code = randomCode();
        String setCode = code + "_" + System.currentTimeMillis();
        //10分钟有效
        redisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone, setCode, 10, TimeUnit.MINUTES);
        thirdPartFeignService.SendCode(phone, code);
        return R.ok();
    }


    //todo 分布式session问题
    //分布式session问题

    @PostMapping("/regist")
    public String regist(@Valid UserRegVo vo, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            // model.addAttribute("errors",errors);
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.mall.com/register.html";
        }

        //注册
        String code = vo.getCode();
        String s = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
        if (StringUtils.isNotEmpty(s)) {
            if (code.equals(s.split("_")[0])) {
                //删除验证码
                redisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
                //验证码通过 //调用远程服务
                R r = memberFeignService.regist(vo);
                if (r.getCode() == 0) {
                    return "redirect:http://auth.mall.com/login.html";
                } else {
                    Map<String, String> errors = new HashMap<>(2);
                    errors.put("msg", r.getData("msg",new TypeReference<String>() {
                    }));
                    redirectAttributes.addFlashAttribute("errors", errors);
                    return "redirect:http://auth.mall.com/register.html";
                }
            } else {
                Map<String, String> errors = new HashMap<>(2);
                errors.put("code", "验证码错误");
                redirectAttributes.addFlashAttribute("errors", errors);
                return "redirect:http://auth.mall.com/register.html";
            }
        } else {
            Map<String, String> errors = new HashMap<>(2);
            errors.put("code", "验证码错误");
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.mall.com/register.html";
        }

    }

    @PostMapping("/login")
    public String login(UserLoginVo vo,RedirectAttributes redirectAttributes){
        //远程登录
        R r = memberFeignService.login(vo);
        if (r.getCode()==0){
            //成功
            //todo 登录成功后的处理
            return "redirect:http://mall.com";
        }else {
            Map<String,String> errors =  new HashMap<>(2);
            errors.put("msg",r.getData("msg",new TypeReference<String>(){}));
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:http://auth.mall.com/login.html";
        }
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
