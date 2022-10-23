package com.epam.jwd.audiotrack_ordering.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Card implements Entity {

    private static final long serialVersionUID = 5338065730502474961L;

    private final Long id;
    private final String holderName;
    private final String number;
    private final BigDecimal amount;

    public Card(Long id, String holderName, String number, BigDecimal amount) {
        this.id = id;
        this.holderName = holderName;
        this.number = number;
        this.amount = amount;
    }

    public Card(String holderName, String number, BigDecimal amount) {
        this(null, holderName, number, amount);
    }

    @Override
    public Long getId() {
        return null;
    }

    public String getHolderName() {
        return holderName;
    }

    public String getNumber() {
        return number;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(id, card.id) &&
                Objects.equals(holderName, card.holderName) &&
                Objects.equals(number, card.number) &&
                Objects.equals(amount, card.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, holderName, number, amount);
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", holderName='" + holderName + '\'' +
                ", number=" + number +
                ", amount=" + amount +
                '}';
    }
}
