package com.epam.jwd.audiotrack_ordering.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Order implements Entity {

    private static final long serialVersionUID = -584871607969181602L;

    private final Long id;
    private final LocalDate date;
    private final Long userId;
    private final StatusOrder status;
    private final BigDecimal value;

    private enum StatusOrder {
        NEW, PLACED, COMPLETED, CONCEALED
    }

    public Order(Long id, LocalDate date, Long userId, StatusOrder status, BigDecimal value) {
        this.id = id;
        this.date = date;
        this.userId = userId;
        this.status = status;
        this.value = value;
    }

    public Order(LocalDate date, Long userId, StatusOrder status, BigDecimal value) {
        this(null, date, userId, status, value);
    }

    @Override
    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getUserId() {
        return userId;
    }

    public StatusOrder getStatus() {
        return status;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
                Objects.equals(date, order.date) &&
                Objects.equals(userId, order.userId) &&
                status == order.status &&
                Objects.equals(value, order.value);
    }

    public static Order.StatusOrder typeOf(String name) {
        for (Order.StatusOrder status : Order.StatusOrder.values()) {
            if (status.name().equalsIgnoreCase(name)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No status found with name: [" + name + "]");
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, userId, status, value);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", date=" + date +
                ", userId=" + userId +
                ", status=" + status +
                ", value=" + value +
                '}';
    }
}
