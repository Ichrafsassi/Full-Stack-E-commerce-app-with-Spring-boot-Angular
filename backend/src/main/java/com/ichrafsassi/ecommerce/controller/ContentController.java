package com.ichrafsassi.ecommerce.controller;

import com.ichrafsassi.ecommerce.dto.SiteContentDto;
import com.ichrafsassi.ecommerce.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @GetMapping
    public List<SiteContentDto> published() {
        return contentService.published();
    }

    @GetMapping("/{key}")
    public SiteContentDto byKey(@PathVariable String key) {
        return contentService.findByKey(key);
    }
}
