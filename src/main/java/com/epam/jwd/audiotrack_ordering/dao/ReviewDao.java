package com.epam.jwd.audiotrack_ordering.dao;

import com.epam.jwd.audiotrack_ordering.dao.impl.MethodReviewDao;
import com.epam.jwd.audiotrack_ordering.entity.Review;

import java.util.List;

public interface ReviewDao extends EntityDao<Review> {

    List<Review> findReviewsByTrackId(Long titleId);

    static ReviewDao getInstance() {
        return MethodReviewDao.getInstance();
    }
}
