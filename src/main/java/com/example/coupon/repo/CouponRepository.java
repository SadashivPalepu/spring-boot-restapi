package com.example.coupon.repo;

import com.example.coupon.model.Coupon;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends MongoRepository<Coupon, String> {
    Optional<Coupon> findByCode(String code);
    List<Coupon> findByLatBetweenAndLonBetween(Double latMin, Double latMax, Double lonMin, Double lonMax);
}
