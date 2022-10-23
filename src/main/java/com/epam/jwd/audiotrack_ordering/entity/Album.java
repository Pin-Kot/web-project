package com.epam.jwd.audiotrack_ordering.entity;

import java.util.Objects;

public class Album implements Entity {

    private static final long serialVersionUID = -3307258511436325318L;

    private final Long id;
    private final String title;
    private final int year;
    private final AlbumType type;

    private enum AlbumType {
        STUDIO, LIVE, COMPILATION
    }

    public Album(Long id, String title, int year, AlbumType type) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.type = type;
    }

    public Album(String title, int year, AlbumType type) {
        this(null, title, year, type);
    }

    @Override
    public Long getId() {
        return null;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public AlbumType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return year == album.year &&
                Objects.equals(id, album.id) &&
                Objects.equals(title, album.title) &&
                type == album.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, year, type);
    }

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", type=" + type +
                '}';
    }
}
