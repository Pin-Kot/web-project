package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.entity.Album;

import java.util.List;
import java.util.Optional;

public interface AlbumService extends EntityService<Album> {

    List<Album> findAlbumsByArtistName(String artistName);

    Optional<Album> findByTitleByYear(String title, int year);

    boolean createByArtistNameWithValidator(String title, String stringYear, String type, String artistName);
}
