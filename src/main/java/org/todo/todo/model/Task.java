package org.todo.todo.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.todo.todo.model.enums.StatusEnum;

import java.time.LocalDate;

@Data
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue
    private long id;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEnum status;

    private String title;

    private LocalDate deadline;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDate createdOn;
}
