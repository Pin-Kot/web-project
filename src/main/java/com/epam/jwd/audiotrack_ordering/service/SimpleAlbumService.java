package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.dao.AlbumDao;
import com.epam.jwd.audiotrack_ordering.entity.Album;
import com.epam.jwd.audiotrack_ordering.validator.MusicEntityValidator;

import java.util.List;
import java.util.Optional;

public class SimpleAlbumService implements AlbumService {

    private final AlbumDao albumDao;

    SimpleAlbumService(AlbumDao albumDao) {
        this.albumDao = albumDao;
    }

    @Override
    public void create(Album entity) {
        albumDao.create(entity);
    }

    @Override
    public boolean createByArtistNameWithValidator(String title, String stringYear, String type,
                                                   String artistName) {
        MusicEntityValidator validator = MusicEntityValidator.getInstance();
        if (validator.isNumeric(stringYear)) {
            final int year = Integer.parseInt(stringYear);
            if (validator.isAlbumDataValid(title, year, type)) {
                if (albumDao.findAll()
                        .stream()
                        .filter(st -> st.getTitle().equals(title))
                        .filter(st -> st.getYear() == year)
                        .noneMatch(st -> st.getType() == Album.typeOf(type))) {
                    if (albumDao.findAlbumsByArtistName(artistName).stream().noneMatch(st -> st.getTitle().equals(title))) {
                        albumDao.create(new Album(title, year, Album.typeOf(type)));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public List<Album> findAll() {
        return albumDao.findAll();
    }

    @Override
    public List<Album> findAlbumsByArtistName(String artistName) {
        return albumDao.findAlbumsByArtistName(artistName);
    }

    @Override
    public Optional<Album> findByTitleByYear(String title, int year) {
        return albumDao.findByTitleByYear(title, year);
    }

    @Override
    public void update(Album entity) {
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
