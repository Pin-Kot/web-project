package com.epam.jwd.audiotrack_ordering.entity;

import java.util.Objects;

public class Image implements Entity {

    private static final long serialVersionUID = -7225327175955476071L;

    private final Long id;
    private final String image;

    public Image(Long id, String image) {
        this.id = id;
        this.image = image;
    }

    public Image(String image) {
        this(null, image);
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image photo = (Image) o;
        return Objects.equals(id, photo.id) &&
                Objects.equals(image, photo.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, image);
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", image='" + image + '\'' +
                '}';
    }
}
