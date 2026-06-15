package com.ichrafsassi.ecommerce.dto;

import com.ichrafsassi.ecommerce.domain.ContentType;
import com.ichrafsassi.ecommerce.domain.SiteContent;

import java.time.Instant;

public record SiteContentDto(Long id, String contentKey, String title, String body, ContentType type,
                             boolean published, Instant updatedAt) {
    public static SiteContentDto from(SiteContent c) {
        return new SiteContentDto(c.getId(), c.getContentKey(), c.getTitle(), c.getBody(), c.getType(), c.isPublished(), c.getUpdatedAt());
    }
}
