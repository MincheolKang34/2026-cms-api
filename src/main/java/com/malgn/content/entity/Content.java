package com.malgn.content.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "contents")
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "view_count", nullable = false)
    private Long viewCount;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "created_by", nullable = false, length = 50)
    private String createdBy;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    @Builder
    public Content(String title, String description, String createdBy) {
        this.title = title;
        this.description = description;
        this.viewCount = 0L;
        this.createdDate = LocalDateTime.now();
        this.createdBy = createdBy;
        this.lastModifiedDate = LocalDateTime.now();
        this.lastModifiedBy = createdBy;
    }

    public void update(String title, String description, String lastModifiedBy) {
        this.title = title;
        this.description = description;
        this.lastModifiedDate = LocalDateTime.now();
        this.lastModifiedBy = lastModifiedBy;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }
}