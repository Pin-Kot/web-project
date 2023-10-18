package com.epam.jwd.audiotrack_ordering.entity;

import java.time.LocalDate;
import java.util.Objects;

public class Review implements Entity {

    private final Long id;
    private final LocalDate date;
    private final String text;
    private final String accountLogin;
    private final Long trackId;

    public Review(Long id, LocalDate date, String text, String accountLogin, Long trackId) {
        this.id = id;
        this.date = date;
        this.text = text;
        this.accountLogin = accountLogin;
        this.trackId = trackId;
    }

    public Review(LocalDate date, String text, String accountLogin, Long trackId) {
        this(null, date, text, accountLogin, trackId);
    }

    @Override
    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public String getAccountLogin() {
        return accountLogin;
    }

    public Long getTrackId() {
        return trackId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(id, review.id) &&
                Objects.equals(date, review.date) &&
                Objects.equals(text, review.text) &&
                Objects.equals(accountLogin, review.accountLogin) &&
                Objects.equals(trackId, review.trackId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, text, accountLogin, trackId);
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", date=" + date +
                ", text='" + text + '\'' +
                ", accountLogin=" + accountLogin +
                ", trackId=" + trackId +
                '}';
    }
}
