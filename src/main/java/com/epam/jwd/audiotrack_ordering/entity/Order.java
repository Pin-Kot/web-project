package com.epam.jwd.audiotrack_ordering.entity;

import java.util.Date;
import java.util.Objects;

public class Order implements Entity {

    private static final long serialVersionUID = 8262672446652403519L;

    private final Long id;
    private final Date date;
    private final Long userId;
    private final StatusOrder status;

    private enum StatusOrder {
        NEW, PLACED, COMPLETED, CONCEALED
    }

    public Order(Long id, Date date, Long userId, StatusOrder status) {
        this.id = id;
        this.date = date;
        this.userId = userId;
        this.status = status;
    }

    public Order(Date date, Long userId, StatusOrder status) {
        this(null, date, userId, status);
    }

    @Override
    public Long getId() {
        return null;
    }

    public Date getDate() {
        return date;
    }

    public Long getUserId() {
        return userId;
    }

    public StatusOrder getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
                Objects.equals(date, order.date) &&
                Objects.equals(userId, order.userId) &&
                status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, userId, status);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", date=" + date +
                ", userId=" + userId +
                ", status=" + status +
                '}';
    }
}
