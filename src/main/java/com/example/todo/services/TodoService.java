package com.example.todo.services;

import com.example.todo.enums.Status;
import com.example.todo.exceptions.UserException;
import com.example.todo.models.TodoItems;
import com.example.todo.models.User;

import java.util.List;

public interface TodoService {
    void save(TodoItems todo, Long userId) throws UserException;
    TodoItems findTodoById(Long id);
    List<TodoItems> findAllUserTodos(Long userId);

    TodoItems findASingleTodoByUserAndId(User user, Long todoId);

    List<TodoItems> findTodoItemsByUserIdAndAndStatus(Long userId, Status status);

    void deleteTodo(Long id);
}
