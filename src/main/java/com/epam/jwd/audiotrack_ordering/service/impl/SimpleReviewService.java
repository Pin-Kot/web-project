package com.epam.jwd.audiotrack_ordering.service.impl;

import com.epam.jwd.audiotrack_ordering.dao.ReviewDao;
import com.epam.jwd.audiotrack_ordering.entity.Review;
import com.epam.jwd.audiotrack_ordering.service.ReviewService;

import java.util.List;
import java.util.Optional;

public class SimpleReviewService implements ReviewService {

    private final ReviewDao reviewDao;

    SimpleReviewService(ReviewDao reviewDao) {
        this.reviewDao = reviewDao;
    }

    @Override
    public void create(Review entity) {
        reviewDao.create(entity);
    }

    @Override
    public List<Review> findReviewsByTrackId(Long trackId) {
        return reviewDao.findReviewsByTrackId(trackId);
    }

    @Override
    public Optional<Review> find(Long id) {
        return reviewDao.find(id);
    }

    @Override
    public List<Review> findAll() {
        return reviewDao.findAll();
    }

    @Override
    public void update(Review entity) {
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
