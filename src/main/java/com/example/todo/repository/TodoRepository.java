package com.example.todo.repository;

import com.example.todo.enums.Status;
import com.example.todo.models.TodoItems;
import com.example.todo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//interface between of java & database

@Repository
public interface TodoRepository extends JpaRepository<TodoItems, Long> {
    @Override
    Optional<TodoItems> findById(Long id);

    List<TodoItems> findAllByUser_Id(Long userId);
    Optional<TodoItems> findTodoItemsByUserAndId(User user, Long id);

    List<TodoItems> findTodoItemsByUserIdAndAndStatus(Long userId, Status status);
}
