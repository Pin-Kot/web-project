package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.dao.AlbumDao;
import com.epam.jwd.audiotrack_ordering.entity.Album;

import java.util.List;
import java.util.Optional;

public class AlbumService implements EntityService<Album> {

    private final AlbumDao albumDao;

    AlbumService(AlbumDao albumDao) {
        this.albumDao = albumDao;
    }

    @Override
    public List<Album> findAll() {
        return albumDao.read();
    }

    @Override
    public Optional<Album> create(Album entity) {
        return Optional.empty();
    }
}
