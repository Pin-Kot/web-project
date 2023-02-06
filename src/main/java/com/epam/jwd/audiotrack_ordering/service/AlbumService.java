package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.dao.AlbumDao;
import com.epam.jwd.audiotrack_ordering.entity.Album;

import java.util.List;

public class AlbumService implements EntityService<Album> {

    private final AlbumDao albumDao;

    AlbumService(AlbumDao albumDao) {
        this.albumDao = albumDao;
    }

    @Override
    public List<Album> findAll() {
        return albumDao.findAll();
    }

    @Override
    public void update(Album entity) {
    }

    @Override
    public void create(Album entity) {}
}
