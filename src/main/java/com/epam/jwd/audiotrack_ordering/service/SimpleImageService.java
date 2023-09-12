package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.dao.ImageDao;
import com.epam.jwd.audiotrack_ordering.entity.Album;
import com.epam.jwd.audiotrack_ordering.entity.Image;

import java.io.InputStream;
import java.util.List;

public class SimpleImageService implements ImageService {

    private final ImageDao imageDao;

    SimpleImageService(ImageDao imageDao) {
        this.imageDao = imageDao;
    }

    @Override
    public void create(Image image) {
        imageDao.create(image);
    }

    @Override
    public List<Image> findAll() {
        return imageDao.findAll();
    }

    @Override
    public void update(Image entity) {
    }

    @Override
    public void createImage(Album album, InputStream image) {
        imageDao.createImage(album, image);
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public List<Image> findImagesByAlbum(String albumTitle) {
        return imageDao.findAlbumImages(albumTitle);
    }
}
