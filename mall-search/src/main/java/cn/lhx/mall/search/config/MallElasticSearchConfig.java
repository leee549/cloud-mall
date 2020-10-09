package cn.lhx.mall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lee549
 * @date 2020/10/6 11:00
 */
@Configuration
public class MallElasticSearchConfig {

    public static final RequestOptions COMMON_OPTIONS;
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        // builder.addHeader("Authorization", "Bearer " + TOKEN);
        // builder.setHttpAsyncResponseConsumerFactory(
        //         new HttpAsyncResponseConsumerFactory
        //                 .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }

    @Bean
    public RestHighLevelClient restHighLevelClient(){
        RestClientBuilder builder=null;
        builder=RestClient.builder(new HttpHost("192.168.56.3",9200,"http"));
        RestHighLevelClient client = new RestHighLevelClient(builder);
            // RestClient.builder(
            //         new HttpHost("192.168.56.3", 9200, "http")));
        return client;
    }
}
