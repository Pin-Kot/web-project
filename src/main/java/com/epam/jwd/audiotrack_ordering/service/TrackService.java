package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.entity.Track;

import java.util.List;

public interface TrackService extends EntityService<Track> {

    List<Track> findTracksByArtistName(String artistName);

    List<Track> findTracksByAlbumTitle(String title);
}
