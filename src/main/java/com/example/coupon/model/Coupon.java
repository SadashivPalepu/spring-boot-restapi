package com.example.coupon.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "coupons")
public class Coupon {
    @Id
    private String id;
    private String code;
    private String sharedBy;
    private String claimedBy;
    private Boolean claimed;
    private Instant createdAt;
    private Instant claimedAt;
    private String shareUrl;
    private Double lat;
    private Double lon;
    private String title;
}
