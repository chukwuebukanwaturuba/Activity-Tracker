package dto;

import com.example.todo.enums.Status;
import com.example.todo.models.TodoItems;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class TodoDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant completedAt;

    public TodoDTO(TodoItems todoItems) {
        this.id = todoItems.getId();
        this.title = todoItems.getTitle();
        this.description = todoItems.getDescription();
        this.status = String.valueOf(todoItems.getStatus());
        this.createdAt = todoItems.getCreatedAt();
        this.updatedAt = todoItems.getUpdatedAt();
        this.completedAt = todoItems.getCompletedAt();
    }
}
