package com.zhang.mysql;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * 数据源测试
 *
 * @author zhangzhe
 * @date 2023-02-16 9:52
 */
@SpringBootTest
@ActiveProfiles("dev")
class DataSourceTest {

    @Autowired
    private DataSource dataSource;

    @Test
    @DisplayName("获取连接信息")
    void should_return_metadata_of_datasource() throws SQLException {
        Connection connection = dataSource.getConnection();
        DatabaseMetaData metaData = connection.getMetaData();

        assertThat(dataSource.getClass().getName(), is(equalTo(HikariDataSource.class.getName())));
        assertThat(metaData.getDriverName(), is(equalTo("MySQL Connector/J")));
        assertThat(metaData.getDatabaseProductName(), is(equalTo("MySQL")));
    }

}
