package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.dao.OrderDao;
import com.epam.jwd.audiotrack_ordering.entity.Order;
import com.epam.jwd.audiotrack_ordering.entity.Track;

import java.util.List;
import java.util.Optional;

public class SimpleOrderService implements OrderService {

    private final OrderDao orderDao;

    public SimpleOrderService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public List<Order> findOrdersByUserId(Long userId) {
        return orderDao.findOrdersByUserId(userId);
    }

    @Override
    public void create(Order order) {
        orderDao.create(order);
    }

    @Override
    public void createOrder(Order order, List<Track> tracks) {
        orderDao.createOrder(order, tracks);
    }

    @Override
    public Optional<Order> find(Long id) {
        return orderDao.find(id);
    }

    @Override
    public List<Order> findAll() {
        return orderDao.findAll();
    }

    @Override
    public void update(Order order) {
        orderDao.update(order);
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}