package com.ichrafsassi.ecommerce.repository;

import com.ichrafsassi.ecommerce.domain.ContentType;
import com.ichrafsassi.ecommerce.domain.SiteContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SiteContentRepository extends JpaRepository<SiteContent, Long> {
    Optional<SiteContent> findByContentKey(String contentKey);
    List<SiteContent> findByPublishedTrue();
    List<SiteContent> findByTypeAndPublishedTrue(ContentType type);
}
