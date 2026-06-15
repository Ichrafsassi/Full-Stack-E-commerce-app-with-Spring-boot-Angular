package com.ichrafsassi.ecommerce.service;

import com.ichrafsassi.ecommerce.domain.SiteContent;
import com.ichrafsassi.ecommerce.dto.SiteContentDto;
import com.ichrafsassi.ecommerce.dto.SiteContentRequest;
import com.ichrafsassi.ecommerce.exception.BadRequestException;
import com.ichrafsassi.ecommerce.exception.ResourceNotFoundException;
import com.ichrafsassi.ecommerce.repository.SiteContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final SiteContentRepository contentRepository;

    public List<SiteContentDto> published() {
        return contentRepository.findByPublishedTrue().stream().map(SiteContentDto::from).toList();
    }

    public List<SiteContentDto> all() {
        return contentRepository.findAll().stream().map(SiteContentDto::from).toList();
    }

    public SiteContentDto findByKey(String key) {
        return SiteContentDto.from(contentRepository.findByContentKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("Content not found")));
    }

    public SiteContentDto create(SiteContentRequest request) {
        if (contentRepository.findByContentKey(request.contentKey()).isPresent()) {
            throw new BadRequestException("Content key already exists");
        }
        return SiteContentDto.from(contentRepository.save(map(new SiteContent(), request)));
    }

    public SiteContentDto update(Long id, SiteContentRequest request) {
        SiteContent content = contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Content not found"));
        return SiteContentDto.from(contentRepository.save(map(content, request)));
    }

    public void delete(Long id) {
        contentRepository.deleteById(id);
    }

    private SiteContent map(SiteContent c, SiteContentRequest r) {
        c.setContentKey(r.contentKey());
        c.setTitle(r.title());
        c.setBody(r.body());
        c.setType(r.type());
        c.setPublished(r.published());
        return c;
    }
}
