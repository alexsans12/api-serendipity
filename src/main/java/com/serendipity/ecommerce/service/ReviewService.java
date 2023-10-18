package com.serendipity.ecommerce.service;

import com.serendipity.ecommerce.domain.Review;

import java.util.Optional;

public interface ReviewService {
    Iterable<Review> getAllReviews();
    Optional<Review> getReviewById(Long id);
    Iterable<Review> getReviewsByProductId(Long productId);
    Review createReview(Review review);
    Review updateReview(Review review);
    void deleteReview(Long id);
}
