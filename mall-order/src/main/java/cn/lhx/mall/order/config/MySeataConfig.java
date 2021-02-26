package cn.lhx.mall.order.config;

import com.zaxxer.hikari.HikariDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author lee549
 * @date 2021/2/24 16:15
 */
@Configuration
public class MySeataConfig {
    @Resource
    private DataSourceProperties dataSourceProperties;

    @Bean
    public DataSource dataSource(DataSourceProperties properties){
        HikariDataSource dataSource = dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        if (StringUtils.hasText(properties.getName())) {
            dataSource.setPoolName(properties.getName());
        }
        return new DataSourceProxy(dataSource);
    }
}
