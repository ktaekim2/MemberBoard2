package com.its.memberboard.entity;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {
    @CreationTimestamp // 새롭게 등록된 시간을 체크
    @Column(updatable = false) // 업데이트 쿼리가 나갔을 때 값을 변경X(insert 때만 관여)
    private LocalDateTime boardCreatedDate;
}
