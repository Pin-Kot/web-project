package com.epam.jwd.audiotrack_ordering.entity;

import java.util.Objects;

public class Artist implements Entity {

    private static final long serialVersionUID = -7377961487231116108L;

    private final Long id;
    private final String name;

    public Artist(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Artist(String name) {
        this(null, name);
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return Objects.equals(id, artist.id) &&
                Objects.equals(name, artist.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
