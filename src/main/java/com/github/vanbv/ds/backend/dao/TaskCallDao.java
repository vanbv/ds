package com.github.vanbv.ds.backend.dao;

import com.github.vanbv.ds.backend.domain.TaskCall;
import com.github.vanbv.ds.backend.domain.TaskCallInfo;
import com.github.vanbv.ds.backend.mapper.TaskCallInfoMapper;
import com.github.vanbv.ds.backend.mapper.TaskCallMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskCallDao {

    private final JdbcTemplate jdbcTemplate;

    public TaskCallDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TaskCall> findByIdUser(int idUser, int limit, int offset) {
        return jdbcTemplate.query(
                "       SELECT tc.*, " +
                        "           o.number AS order_number " +
                        "      FROM task_call tc " +
                        "      JOIN ord o ON (o.id = tc.id_ord) " +
                        "     WHERE tc.id_courier = ? " +
                        " ORDER BY tc.state," +
                        "          tc.id DESC " +
                        "    LIMIT ? " +
                        "   OFFSET ?", new TaskCallMapper(), idUser, limit, offset);
    }

    public int count(int idUser) {
        return jdbcTemplate.queryForObject("SELECT COUNT (tc.*) FROM task_call tc WHERE tc.id_courier = ?",
                Integer.class, idUser);
    }

    public boolean create(String orderNumber, String description, int idUser) {
        return jdbcTemplate.update(
                "INSERT INTO task_call (description, " +
                        "                    id_ord, " +
                        "                    id_courier) " +
                        "  SELECT ?, o.id, ? " +
                        "    FROM ord o " +
                        "   WHERE o.number = ?", description, idUser, orderNumber) > 0;
    }

    public List<TaskCallInfo> findAll(int limit, int offset, String filter) {
        return jdbcTemplate.query(
                "       SELECT tc.*, " +
                        "           o.number AS order_number, " +
                        "           c.email AS client_email, " +
                        "           c.mobile AS client_mobile " +
                        "      FROM task_call tc " +
                        "      JOIN ord o ON (o.id = tc.id_ord) " +
                        "      JOIN client c ON (c.id = o.id_client) " +
                        " WHERE UPPER(o.number) LIKE '%' || UPPER(?) || '%'" +
                        " ORDER BY tc.state," +
                        "          tc.id DESC " +
                        "    LIMIT ? " +
                        "   OFFSET ?", new TaskCallInfoMapper(), filter, limit, offset);
    }

    public int count(String filter) {
        return jdbcTemplate.queryForObject(
                "   SELECT COUNT (tc.*) " +
                        "  FROM task_call tc " +
                        "  JOIN ord o ON (o.id = tc.id_ord) " +
                        " WHERE UPPER(o.number) LIKE '%' || UPPER(?) || '%'", Integer.class, filter);
    }

    public boolean update(int idUser, int id, int state) {
        return jdbcTemplate.update("UPDATE task_call SET state = ?, id_operator = ? WHERE id = ? AND state = ?",
                state, idUser, id, TaskCall.State.NEW.ordinal()) > 0;
    }
}
