package com.junior.domain.admin;

import com.junior.domain.base.BaseEntity;
import com.junior.dto.admin.notice.UpdateNoticeDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    private String title;
    @Column(length = 1000)
    private String content;

    @Builder.Default
    private Boolean isDeleted = false;

    public void softDelete() {
        this.isDeleted = true;
    }

    public void update(UpdateNoticeDto updateNoticeDto) {
        this.title = title;
        this.content = content;
    }
}
