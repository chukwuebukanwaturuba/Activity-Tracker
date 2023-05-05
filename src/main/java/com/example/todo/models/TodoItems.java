package com.example.todo.models;

import com.example.todo.enums.Status;
import dto.TodoDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "todo_items")
public class TodoItems implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant completedAt;

    public TodoItems(TodoDTO todoDTO){
        this.title = todoDTO.getTitle();
        this.description = todoDTO.getDescription();
        this.status = Status.valueOf(todoDTO.getStatus());
        this.createdAt = todoDTO.getCreatedAt();
        this.updatedAt = todoDTO.getUpdatedAt();
        this.completedAt = todoDTO.getCompletedAt();
    }

    @PrePersist
    protected void onCreate(){
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
