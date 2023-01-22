package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.dao.ArtistDao;
import com.epam.jwd.audiotrack_ordering.entity.Artist;

import java.util.List;

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
    public void update(Artist entity) {
    }

    @Override
    public void create(Artist entity) {}
}
