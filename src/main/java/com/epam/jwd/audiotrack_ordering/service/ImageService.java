package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.entity.Album;
import com.epam.jwd.audiotrack_ordering.entity.Image;

import java.io.InputStream;
import java.util.List;

public interface ImageService extends EntityService<Image> {

    void createImage(Album album, InputStream image);

    List<Image> findImagesByAlbum(String albumTitle);
}
