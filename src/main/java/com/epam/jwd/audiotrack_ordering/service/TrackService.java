package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.dao.TrackDao;
import com.epam.jwd.audiotrack_ordering.entity.Track;

import java.util.List;
import java.util.Optional;

public class TrackService implements EntityService<Track> {

    private final TrackDao trackDao;

    TrackService(TrackDao trackDao) {
        this.trackDao = trackDao;
    }

    @Override
    public List<Track> findAll() {
        return trackDao.read();
    }

    @Override
    public Optional<Track> create(Track entity) {
        return Optional.empty();
    }
}
