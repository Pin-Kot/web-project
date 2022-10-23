package com.epam.jwd.audiotrack_ordering.entity;

import java.io.Serializable;
import java.util.Objects;

public class AlbumPhotoLink implements Serializable {

    private static final long serialVersionUID = 596513539556755941L;

    private final Long albumId;
    private final Long photoId;

    public AlbumPhotoLink(Long albumId, Long photoId) {
        this.albumId = albumId;
        this.photoId = photoId;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public Long getPhotoId() {
        return photoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlbumPhotoLink that = (AlbumPhotoLink) o;
        return Objects.equals(albumId, that.albumId) &&
                Objects.equals(photoId, that.photoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(albumId, photoId);
    }

    @Override
    public String toString() {
        return "AlbumPhotoLink{" +
                "albumId=" + albumId +
                ", photoId=" + photoId +
                '}';
    }
}
