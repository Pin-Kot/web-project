package com.epam.jwd.audiotrack_ordering.dao;

import com.epam.jwd.audiotrack_ordering.dao.impl.MethodArtistDao;
import com.epam.jwd.audiotrack_ordering.entity.Artist;

import java.util.Optional;

public interface ArtistDao extends EntityDao<Artist> {

    Optional<Artist> findByName(String name);

    static ArtistDao getInstance() {
        return MethodArtistDao.getInstance();
    }
}
