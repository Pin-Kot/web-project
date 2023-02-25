package com.epam.jwd.audiotrack_ordering.service.relation;

import com.epam.jwd.audiotrack_ordering.dao.relation.LinkDao;
import com.epam.jwd.audiotrack_ordering.entity.dto.AlbumArtistLink;

import java.util.List;
import java.util.Optional;

public class SimpleAlbumArtistLinkService implements AlbumArtistLinkService {

    private final LinkDao<AlbumArtistLink> albumArtistLinkDao;

    public SimpleAlbumArtistLinkService(LinkDao<AlbumArtistLink> linkDao) {
        this.albumArtistLinkDao = linkDao;
    }

    @Override
    public void create(AlbumArtistLink link) {
        albumArtistLinkDao.create(link);
    }

    public void createById(Long id) {
        albumArtistLinkDao.createById(id);
    }

    @Override
    public Optional<AlbumArtistLink> findLink(Long leftEntityId, Long rightEntityId) {
        return albumArtistLinkDao.findLink(leftEntityId, rightEntityId);
    }

    @Override
    public void delete(AlbumArtistLink link) {

    }

    @Override
    public List<AlbumArtistLink> findAlbumArtistLinksByArtistId(Long artistId) {
        return null;
    }

    static AlbumArtistLinkService getInstance() {
        return SimpleAlbumArtistLinkService.Holder.INSTANCE;
    }

    private static class Holder {
        public static final SimpleAlbumArtistLinkService INSTANCE = new SimpleAlbumArtistLinkService(LinkDao
                .getInstance());
    }
}
