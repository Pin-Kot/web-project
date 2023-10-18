package com.epam.jwd.audiotrack_ordering.service;

import com.epam.jwd.audiotrack_ordering.entity.Review;

import java.util.List;

public interface ReviewService extends EntityService<Review> {

    List<Review> findReviewsByTrackId(Long trackId);
}
