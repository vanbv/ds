package com.github.vanbv.ds.backend.mapper;

import com.github.vanbv.ds.backend.domain.TaskCall;
import com.github.vanbv.ds.backend.domain.TaskCallInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskCallInfoMapper implements RowMapper<TaskCallInfo> {

    @Override
    public TaskCallInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        TaskCallInfo taskCallInfo = new TaskCallInfo();
        taskCallInfo.setId(rs.getInt("id"));
        taskCallInfo.setOrderNumber(rs.getString("order_number"));
        taskCallInfo.setState(TaskCall.State.values()[rs.getInt("state")]);
        taskCallInfo.setDescription(rs.getString("description"));
        taskCallInfo.setDateCreate(rs.getTimestamp("date_create"));
        taskCallInfo.setClientEmail(rs.getString("client_email"));
        taskCallInfo.setClientMobile(rs.getString("client_mobile"));
        return taskCallInfo;
    }
}
