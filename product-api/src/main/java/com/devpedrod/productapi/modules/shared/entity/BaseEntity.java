package com.devpedrod.productapi.modules.shared.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import static java.time.LocalDateTime.now;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Where(clause = "disable_at is null")
public abstract class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime disabledAt;
    private Long lastModificationBy;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (Objects.isNull(this.id)) {
            this.createdAt = now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        if (!Objects.isNull(this.createdAt)) {
            this.updatedAt = now();
        }
    }
}
