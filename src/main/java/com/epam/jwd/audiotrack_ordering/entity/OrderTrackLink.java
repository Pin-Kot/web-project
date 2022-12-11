package com.epam.jwd.audiotrack_ordering.entity;

import java.io.Serializable;
import java.util.Objects;

public class OrderTrackLink implements Serializable {

    private static final long serialVersionUID = -780740458448037253L;

    private final Long orderId;
    private final Long audiotrackId;

    public OrderTrackLink(Long orderId, Long audiotrackId) {
        this.orderId = orderId;
        this.audiotrackId = audiotrackId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getAudiotrackId() {
        return audiotrackId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTrackLink that = (OrderTrackLink) o;
        return Objects.equals(orderId, that.orderId) &&
                Objects.equals(audiotrackId, that.audiotrackId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, audiotrackId);
    }

    @Override
    public String toString() {
        return "OrderAudiotrackLink{" +
                "orderId=" + orderId +
                ", audiotrackId=" + audiotrackId +
                '}';
    }
}
