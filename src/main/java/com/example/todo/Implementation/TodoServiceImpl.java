package com.example.todo.Implementation;


//use for interfacing TodoItemsRepo. when we want to add todo.

import com.example.todo.enums.Status;
import com.example.todo.exceptions.UserException;
import com.example.todo.models.TodoItems;
import com.example.todo.models.User;
import com.example.todo.repository.TodoRepository;
import com.example.todo.repository.UserRepository;
import com.example.todo.services.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    @Autowired
    public TodoServiceImpl(TodoRepository todoRepository,
                           UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void save(TodoItems todo, Long userId) throws UserException {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            todo.setUser(userOptional.get());
            todo.setStatus(Status.AWAITING);
            log.info("Todo {}", todo);
            todoRepository.save(todo);
        } else {
            throw new UserException("User not found");
        }
    }

    @Override
    public TodoItems findTodoById(Long id) {
        Optional<TodoItems> todoItemsOptional = todoRepository.findById(id);
        return todoItemsOptional.orElse(null);
    }

    @Override
    public List<TodoItems> findAllUserTodos(Long userId) {
        return todoRepository.findAllByUser_Id(userId);

    }

    @Override
    public TodoItems findASingleTodoByUserAndId(User user, Long todoId){
        Optional<TodoItems> optionalTodoItems = todoRepository.findTodoItemsByUserAndId(user, todoId);
        return optionalTodoItems.orElse(null);
    }

    @Override
    public List<TodoItems> findTodoItemsByUserIdAndAndStatus(Long userId, Status status) {
        return todoRepository.findTodoItemsByUserIdAndAndStatus(userId, status)
                .stream().toList();
    }

    @Override
    public void deleteTodo(Long id) {

        todoRepository.deleteById(id);
    }

}