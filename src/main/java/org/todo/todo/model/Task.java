package org.todo.todo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue
    private long id;
    private String description;
    private String status;
    private String title;
}
//private /*LocalDate*/ Date createdOn;
