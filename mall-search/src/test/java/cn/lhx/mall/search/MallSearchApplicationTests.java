package cn.lhx.mall.search;

import cn.lhx.mall.search.config.MallElasticSearchConfig;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;

import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author lee549
 * @date 2020/10/6 12:18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MallSearchApplicationTests {
    @Resource
    private RestHighLevelClient client;
    @Test
    public void test() throws IOException {
        IndexRequest request = new IndexRequest("users");
        request.id("1");

        User user = new User();
        user.setAge(18);
        user.setUserName("zhangsan");
        user.setGender("男");
        String jsonString = JSON.toJSONString(user);
        request.source(jsonString, XContentType.JSON);
        //执行操作
        IndexResponse indexResponse = client.index(request, MallElasticSearchConfig.COMMON_OPTIONS);
        System.out.println(indexResponse);

    }
    @Data
    public class User{
        private String userName;
        private Integer age;
        private String gender;
    }
}
