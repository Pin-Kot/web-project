package com.epam.jwd.audiotrack_ordering.service.relation;

import com.epam.jwd.audiotrack_ordering.entity.dto.AlbumArtistLink;

import java.util.List;

public interface AlbumArtistLinkService extends LinkService<AlbumArtistLink> {

    List<AlbumArtistLink> findAlbumArtistLinksByArtistId(Long artistId);

    static AlbumArtistLinkService getInstance() {
        return SimpleAlbumArtistLinkService.getInstance();
    }
}
