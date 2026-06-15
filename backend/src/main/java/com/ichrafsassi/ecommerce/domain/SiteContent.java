package com.ichrafsassi.ecommerce.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "site_contents", uniqueConstraints = {
        @UniqueConstraint(columnNames = "content_key")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiteContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_key", nullable = false, unique = true)
    private String contentKey;

    @Column(nullable = false)
    private String title;

    @Column(length = 10000)
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentType type;

    @Builder.Default
    private boolean published = true;

    @UpdateTimestamp
    private Instant updatedAt;
}
