package com.lenin.securedoc.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lenin.securedoc.domain.RequestContext;
import com.lenin.securedoc.exception.ApiException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.AlternativeJdkIdGenerator;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public abstract class Auditable {
    @Id
    @SequenceGenerator(name = "primary_key_seq", sequenceName = "primary_key_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "primary_key_seq")
    @Column(name = "id", updatable = false)
    private Long id;
    private String referenceId = new AlternativeJdkIdGenerator().generateId().toString();
    @NotNull
    private Long updatedBy;
    @NotNull
    private Long createdBy;
    @NotNull
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createAt;
    @NotNull
    @CreatedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updateAt;

    // this method going to be called before any entities are saved in a database
    @PrePersist
    public void beforePersist() {
        var userId = RequestContext.getUserId();
        if (userId == null) {
            throw new ApiException("cannot persist entity without user ID in RequestContext for this thread");}
            setCreateAt(now());
            setCreatedBy(userId);
            setUpdatedBy(userId);
            setUpdateAt(now());

    }

    @PreUpdate
    public void beforeUpdate() {
        var userId = RequestContext.getUserId();
        if (userId == null) {
            throw new ApiException("cannot update entity without user ID in RequestContext for this threed");}
            setUpdatedBy(userId);
            setUpdateAt(now());
    }


}
