package cn.lhx.mall.auth.controller;

import cn.lhx.common.utils.HttpUtils;
import cn.lhx.common.utils.R;
import cn.lhx.mall.auth.feign.MemberFeignService;
import cn.lhx.common.vo.MemberRespVo;
import cn.lhx.mall.auth.vo.WeiboUser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lee549
 * @date 2020/11/16 15:45
 */
@Controller
@Slf4j
public class Oauth2Controller {
    @Resource
    private MemberFeignService memberFeignService;

    @GetMapping("/oauth2.0/weibo/success")
    public String weibo(@RequestParam("code") String code, HttpSession session) throws Exception {
        // https://api.weibo.com/oauth2/access_token?client_id=YOUR_CLIENT_ID&client_secret=YOUR_CLIENT_SECRET&grant_type=authorization_code&redirect_uri=YOUR_REGISTERED_REDIRECT_URI&code=CODE
        Map<String, String> map = new HashMap<>(2);
        map.put("client_id", "3246056366");
        map.put("client_secret", "699af6c49f8326403f9a736d2f2aa98d");
        map.put("grant_type", "authorization_code");
        map.put("redirect_uri", "http://auth.mall.com/oauth2.0/weibo/success");
        map.put("code", code);
        HttpResponse response = HttpUtils.doPost("https://api.weibo.com", "/oauth2/access_token", "post", new HashMap<String,String>(2), null, map);
        if (response.getStatusLine().getStatusCode() == 200) {
            //成功获取Actoken
            String json = EntityUtils.toString(response.getEntity());
            WeiboUser weiboUser = JSON.parseObject(json, WeiboUser.class);
            //1 如果第一次进入自动注册进来 为当前社交用户生成会员信息，以后这个社交账号就对应这个会员信息
            //远程调用会员服务
            R r = memberFeignService.oauth2Login(weiboUser);
            if (r.getCode()==0){
                MemberRespVo data = r.getData("data", new TypeReference<MemberRespVo>() {
                });
                session.setAttribute("loginUser",data);
                log.info("登录成功，返回信息：{}",data);
                //成功回到首页
                return "redirect:http://mall.com";
            }else {
                return "redirect:http://auth.mall.com/login.html";
            }
        } else {
            //否则重新登录
            return "redirect:http://auth.mall.com/login.html";
        }
    }
}
