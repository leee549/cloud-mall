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
        querys.put("content", "【云短信】您的验证码是"+code+"。如非本人操作，请忽略本短信");
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
}
