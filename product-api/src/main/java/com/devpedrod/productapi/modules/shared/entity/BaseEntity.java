package com.devpedrod.productapi.modules.shared.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public abstract class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "sq_seq", allocationSize = 1)
    private Long id;
    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime disabledAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (Objects.isNull(this.id)) {
            this.createdAt = now();
        }
    }
}
