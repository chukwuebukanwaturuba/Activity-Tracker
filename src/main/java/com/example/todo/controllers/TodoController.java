package com.example.todo.controllers;

import com.example.todo.enums.Status;
import com.example.todo.exceptions.UserException;
import com.example.todo.models.TodoItems;
import com.example.todo.models.User;
import com.example.todo.services.TodoService;
import com.example.todo.services.UserService;
import dto.TodoDTO;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.Instant;
import java.util.List;

@Controller
@Slf4j
public class TodoController {
    private final TodoService todoService;
    private final UserService userService;

    @Autowired
    public TodoController(TodoService todoService, UserService userService) {
        this.todoService = todoService;
        this.userService = userService;
    }

    @GetMapping("/todos/dashboard")
    public ModelAndView showDashboard(ModelAndView mav, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        User existingUser = userService.findUserById(userId);
        if (existingUser != null) {
            List<TodoItems> userTodos = todoService.findAllUserTodos(userId);

            List<TodoDTO> userTodoDTOs = userTodos.stream()
                            .map(TodoDTO::new)
                                    .toList();

            mav.addObject("userTodoDTOs", userTodoDTOs);
            log.info("User Todo DTOs size {}", userTodoDTOs.size());
        } else {
            mav.addObject("message", "No tasks found");
        }
        mav.addObject("searchTodoDTO", new TodoDTO());
        mav.setViewName("dashboard");
        return mav;
    }

    @GetMapping("/todos/new")
    public String todoForm(Model model) {
        TodoDTO todoDTO = new TodoDTO();
        model.addAttribute("todoDTO", todoDTO);
        return "add-todo";
    }

    @PostMapping("/todos/save")
    public String createTodo(@Valid @ModelAttribute("todo") TodoDTO todoDTO, BindingResult bindingResult,
                             HttpSession httpSession) throws UserException {
        if (bindingResult.hasErrors()){
            return "add-todo";
        }
        Long userId = (Long) httpSession.getAttribute("userId");
        TodoItems todoItems = new TodoItems(todoDTO);
        todoService.save(todoItems, userId);
        return "redirect:/todos/dashboard";
    }

    @GetMapping("/todos/view/{id}")
    public ModelAndView viewTodo(@PathVariable("id") Long id, HttpSession httpSession) throws UserException{
        ModelAndView mav = new ModelAndView();
        Long userId = (Long) httpSession.getAttribute("userId");
        TodoItems foundTodo = todoService.findASingleTodoByUserAndId(userService.findUserById(userId), id);

        if (userId == null) {
            mav.setViewName("login");
            return mav;
        }

        if (foundTodo != null) {
            mav.addObject("todo", foundTodo);
            mav.setViewName("todo-detail");
        }
        return mav;
    }

    @GetMapping("/todos/edit/{id}")
    public ModelAndView editTodo(@PathVariable("id") Long id,
                                 ModelAndView mav,
                                 HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            mav.setViewName("login");
            return mav;
        }
        TodoItems foundTodo = todoService.findASingleTodoByUserAndId(userService.findUserById(userId), id);

        if (foundTodo != null) {
            TodoDTO todoDTO = new TodoDTO(foundTodo);
            mav.addObject("todoDTO", todoDTO);
            mav.setViewName("edit-todo");
        }
        return mav;
    }

    @PostMapping("/todos/update")
    public ModelAndView updateTodo(@Valid @ModelAttribute("todoDTO") TodoDTO todoDTO,
                                   @RequestParam("id") Long todoId,
                                   HttpSession session) throws UserException {
        ModelAndView mav = new ModelAndView();
        log.info("Update endpoint was hit");
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            mav.setViewName("login");
            return mav;
        }
        TodoItems foundTodo = todoService
                .findASingleTodoByUserAndId(userService.findUserById(userId) ,todoId);

        if (foundTodo != null) {
            foundTodo.setTitle(todoDTO.getTitle());
            foundTodo.setDescription(todoDTO.getDescription());
            foundTodo.setStatus(Status.valueOf(todoDTO.getStatus().toUpperCase()));
            foundTodo.setUpdatedAt(Instant.now());
            todoService.save(foundTodo, userId);
        }
        mav.setViewName("redirect:/todos/dashboard");
        return mav;
    }

    @GetMapping("todos/delete/{id}")
    public ModelAndView deleteTodo(@PathVariable("id") Long id,
                                   ModelAndView mav,
                                   HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            mav.setViewName("login");
            return mav;
        }
        TodoItems foundTodo = todoService
                .findTodoById(id);

        todoService.deleteTodo(foundTodo.getId());
        mav.setViewName("dashboard");
        return mav;
    }
}
