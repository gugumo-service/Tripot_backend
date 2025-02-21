package com.junior.domain.version;

import com.junior.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Version extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "version_id")
    private Long id;

    @Column(length = 15)
    private String version;

    //true일 경우 해당 하위 버전은 해당 버전까지의 강제 업데이트를 요구함
    private Boolean forceUpdate;

    @Enumerated(value = EnumType.STRING)
    private Platform platform;

}
