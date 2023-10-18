package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.dao.ReviewDao;
import com.epam.jwd.audiotrack_ordering.entity.Review;

import java.util.List;

public class SimpleReviewService implements ReviewService {

    private final ReviewDao reviewDao;

    public SimpleReviewService(ReviewDao reviewDao) {
        this.reviewDao = reviewDao;
    }

    @Override
    public List<Review> findReviewsByTrackId(Long trackId) {
        return reviewDao.findReviewsByTrackId(trackId);
    }

    @Override
    public void create(Review entity) {

    }

    @Override
    public List<Review> findAll() {
        return null;
    }

    @Override
    public void update(Review entity) {

    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
