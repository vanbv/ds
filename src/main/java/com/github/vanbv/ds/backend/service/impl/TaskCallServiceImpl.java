package com.github.vanbv.ds.backend.service.impl;

import com.github.vanbv.ds.backend.dao.TaskCallDao;
import com.github.vanbv.ds.backend.domain.TaskCall;
import com.github.vanbv.ds.backend.domain.TaskCallInfo;
import com.github.vanbv.ds.backend.domain.User;
import com.github.vanbv.ds.backend.service.TaskCallService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TaskCallServiceImpl implements TaskCallService {

    private final TaskCallDao taskCallDao;

    public TaskCallServiceImpl(TaskCallDao taskCallDao) {
        this.taskCallDao = taskCallDao;
    }

    @Override
    public List<TaskCall> findByUser(User user, int limit, int offset) {
        if (user != null) {
            return taskCallDao.findByIdUser(user.getId(), limit, offset);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public int count(User user) {
        return taskCallDao.count(user.getId());
    }

    @Override
    public boolean create(User user, TaskCall taskCall) {
        if (user != null && taskCall != null) {
            return taskCallDao.create(taskCall.getOrderNumber(), taskCall.getDescription(), user.getId());
        }

        return false;
    }

    @Override
    public List<TaskCallInfo> findAll(int limit, int offset, String filter) {
        return taskCallDao.findAll(limit, offset, filter);
    }

    @Override
    public int count(String filter) {
        return taskCallDao.count(filter);
    }

    @Override
    public boolean update(User user, TaskCall taskCall) {
        return taskCallDao.update(user.getId(), taskCall.getId(), taskCall.getState().ordinal());
    }
}
