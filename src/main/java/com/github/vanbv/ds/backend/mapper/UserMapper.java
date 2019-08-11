package com.github.vanbv.ds.backend.mapper;

import com.github.vanbv.ds.backend.domain.Role;
import com.github.vanbv.ds.backend.domain.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(Role.values()[rs.getInt("role")]);
        return user;
    }
}
