package cn.lhx.mall.thirdpart;

import cn.lhx.mall.thirdpart.component.SmsComponent;
import cn.lhx.mall.thirdpart.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lee549
 * @date 2020/11/14 10:32
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class test {
    @Autowired
    SmsComponent smsComponent;

    @Test
    public void test(){
        smsComponent.sendSmsCode("13189189106","321345");
    }



}
