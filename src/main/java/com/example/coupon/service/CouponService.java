package com.example.coupon.service;

import com.example.coupon.model.Coupon;
import com.example.coupon.repo.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CouponService {
    private final CouponRepository repo;

    public CouponService(CouponRepository repo) {
        this.repo = repo;
    }

    public Coupon createCoupon(String sharedBy, String title, Double lat, Double lon) {
        Coupon c = Coupon.builder()
                .code(UUID.randomUUID().toString())
                .sharedBy(sharedBy)
                .title(title)
                .claimed(false)
                .createdAt(Instant.now())
                .lat(lat)
                .lon(lon)
                .shareUrl("http://localhost:8082/api/coupons/claim/" + UUID.randomUUID())
                .build();
        return repo.save(c);
    }

    @Transactional
    public Optional<Coupon> claimCoupon(String code, String userId) {
        Optional<Coupon> c = repo.findByCode(code);
        if (c.isEmpty() || Boolean.TRUE.equals(c.get().getClaimed()))
            return Optional.empty();

        Coupon coupon = c.get();
        coupon.setClaimed(true);
        coupon.setClaimedBy(userId);
        coupon.setClaimedAt(Instant.now());
        return Optional.of(repo.save(coupon));
    }

    public List<Coupon> getNearbyCoupons(Double lat, Double lon) {
        // Simple bounding box ±0.05 degrees (~5 km)
        Double latMin = lat - 0.05;
        Double latMax = lat + 0.05;
        Double lonMin = lon - 0.05;
        Double lonMax = lon + 0.05;
        return repo.findByLatBetweenAndLonBetween(latMin, latMax, lonMin, lonMax);
    }
}

