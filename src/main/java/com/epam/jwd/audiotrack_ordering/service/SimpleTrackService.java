package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.dao.TrackDao;
import com.epam.jwd.audiotrack_ordering.entity.Track;

import java.util.List;

public class SimpleTrackService implements TrackService {

    private final TrackDao trackDao;

    SimpleTrackService(TrackDao trackDao) {
        this.trackDao = trackDao;
    }

    @Override
    public void create(Track entity) {
    }

    @Override
    public List<Track> findAll() {
        return trackDao.findAll();
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
