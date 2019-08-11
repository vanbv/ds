package com.github.vanbv.ds.backend.mapper;

import com.github.vanbv.ds.backend.domain.TaskCall;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskCallMapper implements RowMapper<TaskCall> {

    @Override
    public TaskCall mapRow(ResultSet rs, int rowNum) throws SQLException {
        TaskCall taskCall = new TaskCall();
        taskCall.setId(rs.getInt("id"));
        taskCall.setOrderNumber(rs.getString("order_number"));
        taskCall.setState(TaskCall.State.values()[rs.getInt("state")]);
        taskCall.setDescription(rs.getString("description"));
        return taskCall;
    }
}
