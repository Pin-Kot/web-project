package com.epam.jwd.audiotrack_ordering.dao;

import com.epam.jwd.audiotrack_ordering.dao.impl.MethodOrderDao;
import com.epam.jwd.audiotrack_ordering.entity.Order;
import com.epam.jwd.audiotrack_ordering.entity.Track;

import java.util.List;

public interface OrderDao extends EntityDao<Order> {

    List<Order> findOrdersByUserId(Long userId);

    void createOrder(Order order, List<Track> tracks);

    static OrderDao getInstance() {
        return MethodOrderDao.getInstance();
    }
}
