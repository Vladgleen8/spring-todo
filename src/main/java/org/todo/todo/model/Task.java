package org.todo.todo.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Task {
    //private final long id;
    private String description;
    private /*Status*/String status;
    private String title;
    //private /*LocalDate*/ Date createdOn;
}
