package com.serendipity.ecommerce.repository;

import com.serendipity.ecommerce.domain.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<Review, Long> {
    @Query(value = "SELECT r FROM Review r WHERE r.producto.idProducto = ?1")
    Iterable<Review> findAllByProductId(Long productId);
}
