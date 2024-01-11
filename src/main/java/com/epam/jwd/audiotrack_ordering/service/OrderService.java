package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.entity.Order;
import com.epam.jwd.audiotrack_ordering.entity.Track;

import java.util.List;

public interface OrderService extends EntityService<Order> {

    void createOrder(Order order, List<Track> tracks);

    List<Order> findOrdersByUserId(Long userId);
}
