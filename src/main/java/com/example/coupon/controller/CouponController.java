package com.example.coupon.controller;

import com.example.coupon.model.Coupon;
import com.example.coupon.service.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/coupons")
@CrossOrigin(origins = "*")  // For testing: allow React frontend CORS
public class CouponController {

    private final CouponService service;

    public CouponController(CouponService service) {
        this.service = service;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCoupon(@RequestParam("image") MultipartFile file,
                                               @RequestParam("title") String title,
                                               @RequestParam("store") String store,
                                               @RequestParam("lat") Double lat,
                                               @RequestParam("lon") Double lon) {
        try {
            Coupon coupon = service.createCoupon(store, title, lat, lon);
            // Save image locally (demo purpose)
            String uploadDir = "uploads";
            new File(uploadDir).mkdirs();
            String filepath = uploadDir + "/" + file.getOriginalFilename();
            file.transferTo(new File(filepath));
            return ResponseEntity.ok("Coupon uploaded with code: " + coupon.getCode());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Upload failed");
        }
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<Coupon>> nearbyCoupons(@RequestParam Double lat, @RequestParam Double lon) {
        List<Coupon> nearby = service.getNearbyCoupons(lat, lon);
        return ResponseEntity.ok(nearby);
    }

    @PostMapping("/claim")
    public ResponseEntity<?> claim(@RequestParam String code, @RequestParam String userId) {
        Optional<Coupon> claimed = service.claimCoupon(code, userId);
        if (claimed.isPresent()) {
            return ResponseEntity.ok(claimed.get());
        } else {
            return ResponseEntity.badRequest().body("Invalid or already claimed coupon.");
        }
    }
    @PostMapping("/claim/{code}")
    public ResponseEntity<?> claimWithPath(@PathVariable String code,
                                           @RequestParam String userId) {
        return claim(code, userId);
    }
}

