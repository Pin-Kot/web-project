package com.epam.jwd.audiotrack_ordering.dao;

import com.epam.jwd.audiotrack_ordering.entity.Album;
import com.epam.jwd.audiotrack_ordering.entity.Image;

import java.io.InputStream;
import java.util.List;

public interface ImageDao extends EntityDao<Image> {

    List<Image> findAlbumImages(String albumTitle);

    void createImage(Album album, InputStream image);

    static ImageDao getInstance() {
            return MethodImageDao.getInstance();
        }
}
