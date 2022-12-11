package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.dao.ArtistDao;
import com.epam.jwd.audiotrack_ordering.entity.Artist;

import java.util.List;
import java.util.Optional;

public class ArtistService implements EntityService<Artist> {

    private final ArtistDao artistDao;

    ArtistService(ArtistDao artistDao) {
        this.artistDao = artistDao;
    }

    @Override
    public List<Artist> findAll() {
        return artistDao.read();
    }

    @Override
    public Optional<Artist> create(Artist entity) {
        return Optional.empty();
    }
}
