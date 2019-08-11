package com.github.vanbv.ds.backend.dao;

import com.github.vanbv.ds.backend.domain.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public OrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isExist(String number) {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT (o.id) FROM ord o WHERE o.number = ? AND o.state = ?",
                Integer.class, number, Order.State.DELIVERY_PROCESS.ordinal()) > 0;
    }
}
