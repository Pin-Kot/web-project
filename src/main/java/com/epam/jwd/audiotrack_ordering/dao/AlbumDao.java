package com.epam.jwd.audiotrack_ordering.dao;

import com.epam.jwd.audiotrack_ordering.entity.Album;

import java.util.Optional;

public interface AlbumDao extends EntityDao<Album> {

    Optional<Album> findByTitle(String title);

    static AlbumDao getInstance() {
        return MethodAlbumDao.getInstance();
    }
}
