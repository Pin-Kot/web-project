package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.dao.TrackDao;
import com.epam.jwd.audiotrack_ordering.entity.Album;
import com.epam.jwd.audiotrack_ordering.entity.Artist;
import com.epam.jwd.audiotrack_ordering.entity.Track;
import com.epam.jwd.audiotrack_ordering.validator.MusicEntityValidator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class SimpleTrackService implements TrackService {

    private final TrackDao trackDao;

    SimpleTrackService(TrackDao trackDao) {
        this.trackDao = trackDao;
    }

    @Override
    public void create(Track entity) {
    }

    @Override
    public boolean createByArtistByAlbumWithValidator(Artist artist, Album album, String title, String strYear,
                                                      String strPrice) {
        MusicEntityValidator validator = MusicEntityValidator.getInstance();
        if (validator.isTrackDataValid(title, strYear, strPrice)){
            final int year = Integer.parseInt(strYear);
            if (trackDao.findTracksByArtistName(artist.getName())
                    .stream()
                    .filter(st -> st.getTitle().equals(title))
                    .noneMatch(st -> st.getYear() == year)) {
                if (trackDao.findTracksByAlbumTitle(album.getTitle())
                        .stream()
                        .filter(st -> st.getTitle().equals(title))
                        .noneMatch(st -> st.getYear() == year)) {
                    trackDao.createTrack(new Track(title, year, new BigDecimal(strPrice)), album, artist);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<Track> findAll() {
        return trackDao.findAll();
    }

    @Override
    public Optional<Track> findByTitleByYearByPrice(String title, int year, BigDecimal price) {
        return trackDao.findByTitleByYearByPrice(title, year, price);
    }

    @Override
    public List<Track> findTracksByArtistName(String artistName) {
        return trackDao.findTracksByArtistName(artistName);
    }

    @Override
    public List<Track> findTracksByAlbumTitle(String title) {
        return trackDao.findTracksByAlbumTitle(title);
    }

    @Override
    public void update(Track entity) {
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
