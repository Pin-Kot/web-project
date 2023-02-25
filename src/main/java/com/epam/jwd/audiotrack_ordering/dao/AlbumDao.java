package com.epam.jwd.audiotrack_ordering.dao;

import com.epam.jwd.audiotrack_ordering.entity.Album;

import java.util.List;
import java.util.Optional;

public interface AlbumDao extends EntityDao<Album> {

    Optional<Album> findByTitleByYear(String title, int year);

    List<Album> findAlbumsByArtistName(String artistName);

    static AlbumDao getInstance() {
        return MethodAlbumDao.getInstance();
    }
}
