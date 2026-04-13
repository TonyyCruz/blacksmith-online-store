package com.anthony.blacksmithOnlineStore.repository;

import com.anthony.blacksmithOnlineStore.entity.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {

  Page<Rating> findAllByReviewedItemId(Long reviewedItemId, Pageable pageable);
}
