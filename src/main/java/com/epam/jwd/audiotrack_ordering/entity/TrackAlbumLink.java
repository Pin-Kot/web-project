package com.epam.jwd.audiotrack_ordering.entity;

import java.io.Serializable;
import java.util.Objects;

public class TrackAlbumLink implements Serializable {

    private static final long serialVersionUID = -7684857764728016367L;

    private final Long audiotrackId;
    private final Long albumId;

    public TrackAlbumLink(Long audiotrackId, Long albumId) {
        this.audiotrackId = audiotrackId;
        this.albumId = albumId;
    }

    public Long getAudiotrackId() {
        return audiotrackId;
    }

    public Long getAlbumId() {
        return albumId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackAlbumLink that = (TrackAlbumLink) o;
        return Objects.equals(audiotrackId, that.audiotrackId) &&
                Objects.equals(albumId, that.albumId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(audiotrackId, albumId);
    }

    @Override
    public String toString() {
        return "AudiotrackAlbumLink{" +
                "audiotrackId=" + audiotrackId +
                ", albumId=" + albumId +
                '}';
    }
}
