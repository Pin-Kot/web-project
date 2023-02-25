package com.epam.jwd.audiotrack_ordering.entity.dto;

import java.util.Objects;

public class AlbumArtistLink implements Link {

    private static final long serialVersionUID = -1182376046470381143L;
    private final Long leftEntityId;
    private final Long rightEntityId;

    public AlbumArtistLink(Long leftEntityId, Long rightEntityId) {
        this.leftEntityId = leftEntityId;
        this.rightEntityId = rightEntityId;
    }

    @Override
    public Long getLeftEntityId() {
        return leftEntityId;
    }

    @Override
    public Long getRightEntityId() {
        return rightEntityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlbumArtistLink link = (AlbumArtistLink) o;
        return Objects.equals(leftEntityId, link.leftEntityId) &&
                Objects.equals(rightEntityId, link.rightEntityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftEntityId, rightEntityId);
    }

    @Override
    public String toString() {
        return "Link{" +
                "leftEntityId=" + leftEntityId +
                ", rightEntityId=" + rightEntityId +
                '}';
    }
}
