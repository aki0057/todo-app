package com.example.todoapp.infrastructure.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@IdClass(TodoHistoryId.class)
@Table(name = "todo_history")
public class TodoHistoryEntity {

    @Id
    @Column(name = "internal_id")
    private Integer internalId;

    @Id
    @Column(name = "version_number")
    private Integer versionNumber;

    @Column(name = "public_id")
    private String publicId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "detail")
    private String detail;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "completed_flag", nullable = false)
    private boolean completedFlag;

    @Column(name = "deleted_flag", nullable = false)
    private boolean deletedFlag;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
