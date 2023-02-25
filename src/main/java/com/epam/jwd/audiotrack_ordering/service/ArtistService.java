package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.entity.Artist;

import java.util.List;
import java.util.Optional;

public interface ArtistService extends EntityService<Artist> {

    List<Artist> findArtistsByAlbum(String albumTitle);

    Optional<Artist> findByName(String name);
}
