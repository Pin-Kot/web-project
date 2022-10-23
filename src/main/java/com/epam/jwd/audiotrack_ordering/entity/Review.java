package com.epam.jwd.audiotrack_ordering.entity;

import java.util.Date;
import java.util.Objects;

public class Review implements Entity {

    private final Long id;
    private final Date date;
    private final String text;
    private final Long userId;
    private final Long audiotrackId;

    public Review(Long id, Date date, String text, Long userId, Long audiotrackId) {
        this.id = id;
        this.date = date;
        this.text = text;
        this.userId = userId;
        this.audiotrackId = audiotrackId;
    }

    public Review(Date date, String text, Long userId, Long audiotrackId) {
        this(null, date, text, userId, audiotrackId);
    }

    @Override
    public Long getId() {
        return null;
    }

    public Date getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getAudiotrackId() {
        return audiotrackId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(id, review.id) &&
                Objects.equals(date, review.date) &&
                Objects.equals(text, review.text) &&
                Objects.equals(userId, review.userId) &&
                Objects.equals(audiotrackId, review.audiotrackId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, text, userId, audiotrackId);
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", date=" + date +
                ", text='" + text + '\'' +
                ", userId=" + userId +
                ", audiotrackId=" + audiotrackId +
                '}';
    }
}
