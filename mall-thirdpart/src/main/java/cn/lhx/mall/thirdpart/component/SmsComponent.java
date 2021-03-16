package cn.lhx.mall.thirdpart.component;

import cn.lhx.mall.thirdpart.utils.HttpUtils;
import lombok.Data;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lee549
 * @date 2020/11/14 12:34
 */
@Component
@Data
@ConfigurationProperties("sms")
public class SmsComponent {
    private String host;
    private String path ;
    private String appcode;

    public void sendSmsCode(String phone,String code){
        String method = "GET";
        Map<String, String> headers = new HashMap<String, String>(6);
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>(6);
        querys.put("content", "【验证码】尊敬的用户,您的验证码为:"+code+",10分钟后过期,请勿泄露于他人!");
        querys.put("mobile", phone);
        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            System.out.println(response.toString());
            //获取response的body
            // System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendSmsCode2(String phone,String code){
        String host = "https://gyytz.market.alicloudapi.com";
        String path = "/sms/smsSend";
        String method = "POST";
        String appcode = "dff3c31cbd404f30818559f621e55e64";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phone);
        querys.put("param", "**code**:"+code+",**minute**:5");
        querys.put("smsSignId", "2e65b1bb3d054466b82f0c9d125465e2");
        querys.put("templateId", "a09602b817fd47e59e7c6e603d3f088d");
        Map<String, String> bodys = new HashMap<String, String>();
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
