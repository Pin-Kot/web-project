package com.epam.jwd.audiotrack_ordering.entity;

import java.io.Serializable;
import java.util.Objects;

public class AudiotrackArtistLink implements Serializable {

    private static final long serialVersionUID = 1977367036569161910L;

    private final Long audiotrackId;
    private final Long artistId;

    public AudiotrackArtistLink(Long audiotrackId, Long artistId) {
        this.audiotrackId = audiotrackId;
        this.artistId = artistId;
    }

    public Long getAudiotrackId() {
        return audiotrackId;
    }

    public Long getArtistId() {
        return artistId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AudiotrackArtistLink that = (AudiotrackArtistLink) o;
        return Objects.equals(audiotrackId, that.audiotrackId) &&
                Objects.equals(artistId, that.artistId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(audiotrackId, artistId);
    }

    @Override
    public String toString() {
        return "AudiotrackArtistLink{" +
                "audiotrackId=" + audiotrackId +
                ", artistId=" + artistId +
                '}';
    }
}
