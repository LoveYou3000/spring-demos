package com.zhang.mysql;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

/**
 * JdbcTemplate测试
 *
 * @author zhangzhe
 * @date 2023-02-16 11:50
 */
@SpringBootTest
@Transactional
@ActiveProfiles("dev")
class JdbcTemplateTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("增-sql方式")
    void should_return_ok_when_insert_data_with_full_sql() {
        int save = jdbcTemplate.update("insert into sys_role value (100, 'jdbcTemplate插入测试', 0)");

        assertThat(save, greaterThan(0));
    }

    @Test
    @DisplayName("增-带占位符的sql方式")
    void should_return_ok_when_insert_data_with_placeholder_sql() {
        int save = jdbcTemplate.update("insert into sys_role value (?, ?, ?)", 100, "jdbcTemplate插入测试", 0);

        assertThat(save, greaterThan(0));
    }

    @Test
    @DisplayName("增-statement方式1")
    void should_return_ok_when_insert_data_with_statement_1() {
        int save = jdbcTemplate.update("insert into sys_role value (?, ?, ?)", ps -> {
            ps.setInt(1, 100);
            ps.setString(2, "jdbcTemplate插入测试");
            ps.setInt(3, 0);
        });

        assertThat(save, greaterThan(0));
    }

    @Test
    @DisplayName("增-statement方式2")
    void should_return_ok_when_insert_data_with_statement_2() {
        int save = jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("insert into sys_role value (?, ?, ?)");
            ps.setInt(1, 100);
            ps.setString(2, "jdbcTemplate插入测试");
            ps.setInt(3, 0);
            return ps;
        });

        assertThat(save, greaterThan(0));
    }

    @Test
    @DisplayName("增-返回自增主键")
    void should_return_auto_increment_primary_key_when_insert_data() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int save = jdbcTemplate.update(con -> {
            // 指定主键
            PreparedStatement ps = con.prepareStatement("insert into ana_group(user_id, group_name) value (?, ?)", new String[]{"group_id"});
            ps.setInt(1, 100);
            ps.setString(2, "jdbcTemplate插入测试");
            return ps;
        }, keyHolder);

        assertThat(save, greaterThan(0));
        assertThat(keyHolder.getKey(), notNullValue());
        assertThat(keyHolder.getKey().intValue(), greaterThan(0));
    }

    @Test
    @DisplayName("增-批量sql方式")
    void should_return_ok_when_batch_insert_data_with_full_sql() {
        int[] save = jdbcTemplate.batchUpdate("insert into sys_role values (101, 'jdbcTemplate插入测试1', 0), (102, 'jdbcTemplate插入测试2', 0)");

        assertThat(save, notNullValue());
        for (int i : save) {
            assertThat(i, greaterThan(0));
        }
    }

    @Test
    @DisplayName("增-批量带占位符的sql方式")
    void should_return_ok_when_batch_insert_data_with_placeholder_sql() {
        int[] save = jdbcTemplate.batchUpdate("insert into sys_role value (?, ?, ?)",
                List.of(new Object[]{101, "jdbcTemplate插入测试1", 0}, new Object[]{102, "jdbcTemplate插入测试2", 0}));

        assertThat(save, notNullValue());
        for (int i : save) {
            assertThat(i, greaterThan(0));
        }
    }

    @Test
    @DisplayName("增-批量statement方式")
    void should_return_ok_when_batch_insert_data_with_statement() {
        int[] save = jdbcTemplate.batchUpdate("insert into sys_role value (?, ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                if (i == 0) {
                    ps.setInt(1, 101);
                    ps.setString(2, "jdbcTemplate插入测试1");
                } else if (i == 1) {
                    ps.setInt(1, 102);
                    ps.setString(2, "jdbcTemplate插入测试2");
                }
                ps.setInt(3, 0);
            }

            @Override
            public int getBatchSize() {
                return 2;
            }
        });

        assertThat(save, notNullValue());
        for (int i : save) {
            assertThat(i, greaterThan(0));
        }
    }

}
