package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.dao.ArtistDao;
import com.epam.jwd.audiotrack_ordering.entity.Artist;

import java.util.List;
import java.util.Optional;

public class SimpleArtistService implements ArtistService {

    private final ArtistDao artistDao;

    SimpleArtistService(ArtistDao artistDao) {
        this.artistDao = artistDao;
    }

    @Override
    public List<Artist> findAll() {
        return artistDao.findAll();
    }

    @Override
    public void update(Artist artist) {
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public void create(Artist artist) {
        artistDao.create(artist);
    }

    @Override
    public List<Artist> findArtistsByAlbum(String albumTitle) {
        return null;
    }

    @Override
    public Optional<Artist> findByName(String name) {
        return artistDao.findByName(name);
    }
}