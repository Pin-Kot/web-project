package com.epam.jwd.audiotrack_ordering.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Audiotrack implements Entity {

    private static final long serialVersionUID = -3646092231313315048L;

    private final Long id;
    private final String title;
    private final int year;
    private final BigDecimal price;

    public Audiotrack(Long id, String title, int year, BigDecimal price) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.price = price;
    }

    public Audiotrack(String title, int year, BigDecimal price) {
        this(null, title, year, price);
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Audiotrack that = (Audiotrack) o;
        return year == that.year &&
                Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, year, price);
    }

    @Override
    public String toString() {
        return "Audiotrack{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", price=" + price +
                '}';
    }
}
