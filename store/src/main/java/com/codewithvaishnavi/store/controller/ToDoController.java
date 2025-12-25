package com.codewithvaishnavi.store.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import com.codewithvaishnavi.store.model.ToDo;
import com.codewithvaishnavi.store.model.User;
import com.codewithvaishnavi.store.service.ToDoService;
import com.codewithvaishnavi.store.repository.UserRepository;
import java.security.Principal;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin
public class ToDoController {
    
    private final ToDoService service;
    private final UserRepository userRepo; // Needed to link tasks to users

    public ToDoController(ToDoService service, UserRepository userRepo) {
        this.service = service;
        this.userRepo = userRepo;
    }

    @GetMapping
    public Page<ToDo> getToDos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Principal principal) {
        User user = userRepo.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return service.getAllToDos(user, page, size);
    }

    @PostMapping
    public ToDo addToDo(@RequestBody ToDo todo, Principal principal) {
        // Automatically assign the logged-in user to the task
        String username = principal.getName();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        todo.setUser(user); // Links the task to the current user
        return service.addToDo(todo);
    }

    @PutMapping("/{id}")
    public ToDo updateTodo(@PathVariable Long id, @RequestBody ToDo todo) {
        return service.updateTodo(id, todo);
    }

    @DeleteMapping("/{id}")
    public void deleteToDo(@PathVariable Long id) {
        service.deleteToDo(id);
    }

    @PutMapping("/{id}/complete")
    public ToDo markCompleted(@PathVariable Long id) {
        return service.markCompleted(id);
    }

    @GetMapping("/search")
    public Page<ToDo> searchByTitle(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Principal principal) {
        User user = userRepo.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return service.searchByTitle(title, user, page, size);
    }
}