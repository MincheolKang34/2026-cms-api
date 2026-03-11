package com.malgn.content.service;

import com.malgn.configure.security.SecurityUtils;
import com.malgn.content.dto.ContentCreateRequest;
import com.malgn.content.dto.ContentPageResponse;
import com.malgn.content.dto.ContentResponse;
import com.malgn.content.dto.ContentUpdateRequest;
import com.malgn.content.entity.Content;
import com.malgn.content.repository.ContentRepository;
import com.malgn.global.exception.ContentNotFoundException;
import com.malgn.global.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentService {

    private final ContentRepository contentRepository;

    @Transactional
    public ContentResponse create(ContentCreateRequest request) {
        String currentUsername = SecurityUtils.getCurrentUsername();

        Content content = Content.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .createdBy(currentUsername)
                .build();

        Content savedContent = contentRepository.save(content);
        return ContentResponse.from(savedContent);
    }

    public ContentPageResponse getAll(Pageable pageable) {
        Page<Content> pageResult = contentRepository.findAll(pageable);

        return new ContentPageResponse(
                pageResult.getContent().stream()
                        .map(ContentResponse::from)
                        .toList(),
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isFirst(),
                pageResult.isLast()
        );
    }

    @Transactional
    public ContentResponse getById(Long id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new ContentNotFoundException(id));

        content.increaseViewCount();
        return ContentResponse.from(content);
    }

    @Transactional
    public ContentResponse update(Long id, ContentUpdateRequest request) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new ContentNotFoundException(id));

        validateOwnerOrAdmin(content);

        String currentUsername = SecurityUtils.getCurrentUsername();

        content.update(
                request.getTitle(),
                request.getDescription(),
                currentUsername
        );

        return ContentResponse.from(content);
    }

    @Transactional
    public void delete(Long id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new ContentNotFoundException(id));

        validateOwnerOrAdmin(content);
        contentRepository.delete(content);
    }

    private void validateOwnerOrAdmin(Content content) {
        String currentUsername = SecurityUtils.getCurrentUsername();

        if (SecurityUtils.isAdmin()) {
            return;
        }

        if (!currentUsername.equals(content.getCreatedBy())) {
            throw new ForbiddenException("해당 콘텐츠에 대한 권한이 없습니다.");
        }
    }
}