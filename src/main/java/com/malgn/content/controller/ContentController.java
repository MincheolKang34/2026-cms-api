package com.malgn.content.controller;

import com.malgn.content.dto.ContentCreateRequest;
import com.malgn.content.dto.ContentPageResponse;
import com.malgn.content.dto.ContentResponse;
import com.malgn.content.dto.ContentUpdateRequest;
import com.malgn.content.service.ContentService;
import com.malgn.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @PostMapping
    public ResponseEntity<ApiResponse<ContentResponse>> create(
            @Valid @RequestBody ContentCreateRequest request
    ) {
        ContentResponse response = contentService.create(request);
        return ResponseEntity.ok(ApiResponse.ok("콘텐츠 등록 성공", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ContentPageResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        ContentPageResponse response = contentService.getAll(pageable);
        return ResponseEntity.ok(ApiResponse.ok("콘텐츠 목록 조회 성공", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ContentResponse>> getById(@PathVariable Long id) {
        ContentResponse response = contentService.getById(id);
        return ResponseEntity.ok(ApiResponse.ok("콘텐츠 단건 조회 성공", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ContentResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ContentUpdateRequest request
    ) {
        ContentResponse response = contentService.update(id, request);
        return ResponseEntity.ok(ApiResponse.ok("콘텐츠 수정 성공", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        contentService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("콘텐츠 삭제 성공", null));
    }
}