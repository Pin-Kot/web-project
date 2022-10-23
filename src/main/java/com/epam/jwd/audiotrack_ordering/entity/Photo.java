package com.epam.jwd.audiotrack_ordering.entity;

import java.util.Objects;

public class Photo implements Entity {

    private static final long serialVersionUID = -7225327175955476071L;

    private final Long id;
    private final String filePath;

    public Photo(Long id, String filePath) {
        this.id = id;
        this.filePath = filePath;
    }

    public Photo(String filePath) {
        this(null, filePath);
    }

    @Override
    public Long getId() {
        return null;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo = (Photo) o;
        return Objects.equals(id, photo.id) &&
                Objects.equals(filePath, photo.filePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, filePath);
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
