package com.github.vanbv.ds.backend.service;

import com.github.vanbv.ds.backend.domain.TaskCall;
import com.github.vanbv.ds.backend.domain.TaskCallInfo;
import com.github.vanbv.ds.backend.domain.User;

import java.util.List;

public interface TaskCallService {

    List<TaskCall> findByUser(User user, int limit, int offset);

    int count(User user);

    boolean create(User user, TaskCall taskCall);

    List<TaskCallInfo> findAll(int limit, int offset, String filter);

    int count(String filter);

    boolean update(User user, TaskCall taskCall);
}
