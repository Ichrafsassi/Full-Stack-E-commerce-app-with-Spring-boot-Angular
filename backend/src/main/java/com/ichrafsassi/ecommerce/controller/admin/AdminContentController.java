package com.ichrafsassi.ecommerce.controller.admin;

import com.ichrafsassi.ecommerce.dto.SiteContentDto;
import com.ichrafsassi.ecommerce.dto.SiteContentRequest;
import com.ichrafsassi.ecommerce.service.ContentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/content")
@RequiredArgsConstructor
public class AdminContentController {

    private final ContentService contentService;

    @GetMapping
    public List<SiteContentDto> all() {
        return contentService.all();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SiteContentDto create(@Valid @RequestBody SiteContentRequest request) {
        return contentService.create(request);
    }

    @PutMapping("/{id}")
    public SiteContentDto update(@PathVariable Long id, @Valid @RequestBody SiteContentRequest request) {
        return contentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        contentService.delete(id);
    }
}
