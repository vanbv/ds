package com.github.vanbv.ds.backend.service.impl;

import com.github.vanbv.ds.backend.dao.OrderDao;
import com.github.vanbv.ds.backend.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;

    public OrderServiceImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public boolean isExist(String number) {
        return orderDao.isExist(number);
    }
}
