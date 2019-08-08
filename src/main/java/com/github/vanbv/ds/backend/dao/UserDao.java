package com.github.vanbv.ds.backend.dao;

import com.github.vanbv.ds.backend.domain.User;
import com.github.vanbv.ds.backend.mapper.UserMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User findByEmail(String email) {
        return jdbcTemplate.queryForObject("SELECT u.* FROM usr u WHERE u.email = ?", new UserMapper(), email);
    }
}
