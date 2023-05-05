package com.example.todo.controllers;

import com.example.todo.models.TodoItems;
import com.example.todo.models.User;
import com.example.todo.services.TodoService;
import com.example.todo.services.UserService;
import dto.TodoDTO;
import dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


//to access our todo items

@Controller
public class UserController {
  //private final UserServiceImple userService;

    private final UserService userService;
    private final TodoService todoService;

    @Autowired
    public UserController(UserService userService, TodoService todoService) {
        this.userService = userService;
        this.todoService = todoService;
    }

    @GetMapping("/")//lo:8080
    public String getRegisterPage(Model model){
      model.addAttribute("user", new UserDTO());
      return "register";
  }

  @PostMapping("/register")//:8080/register
    public String register(@ModelAttribute(value = "user") UserDTO userDTO) {
      System.out.println("register request: " + userDTO);
      try{
          User registeredUser = userService.registerUser(userDTO);
          if (registeredUser != null){
              return "redirect:/";
          }
      } catch (RuntimeException e) {
          e.printStackTrace();
      }
      return "register";
  }

  @PostMapping("/login")
    public String login(@ModelAttribute(value = "user")UserDTO userDTO,
                        Model model,
                        HttpSession session){
        User authenticated = userService.authenticate(userDTO);
        if(authenticated != null){
            model.addAttribute("email", authenticated.getEmail());
            session.setAttribute("userId", authenticated.getId());
            Long userId = authenticated.getId();
            List<TodoItems> userTodos = todoService.findAllUserTodos(userId);
            List<TodoDTO> userTodoDTO = userTodos
                    .stream().map(TodoDTO::new).toList();

            model.addAttribute("todos", userTodoDTO);
            return "redirect:/todos/dashboard";
        }
        return "error_page";
  }

  @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest httpServletRequest){
      HttpSession session = httpServletRequest.getSession();
      session.invalidate();
      ModelAndView modelAndView = new ModelAndView();
      modelAndView.setViewName("register");
      modelAndView.addObject("user", new UserDTO());
      return modelAndView;

  }
}
