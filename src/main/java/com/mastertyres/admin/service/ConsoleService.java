package com.mastertyres.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ConsoleService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Object ejecutarConsulta(String sql) {

        String query = sql.trim().toLowerCase();

        if (query.startsWith("select")) {

            return jdbcTemplate.queryForList(sql);

        } else {

            int affectedRows = jdbcTemplate.update(sql);

            return affectedRows;
        }
    }

}
