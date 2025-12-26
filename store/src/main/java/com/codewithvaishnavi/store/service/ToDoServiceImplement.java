package com.codewithvaishnavi.store.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.codewithvaishnavi.store.model.ToDo;
import com.codewithvaishnavi.store.model.User;
import com.codewithvaishnavi.store.repository.ToDoRepository;

@Service
public class ToDoServiceImplement implements ToDoService {

    private final ToDoRepository repo;
    
    // Constructor injection is the best practice
    public ToDoServiceImplement(ToDoRepository repo) {
        this.repo = repo;
    }
    
    @Override
    public ToDo addToDo(ToDo todo) {
        todo.setStatus(ToDo.Status.PENDING);
        return repo.save(todo);
    }

    @Override
    public ToDo updateTodo(Long id, ToDo todo) {
        ToDo existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("ToDo not found"));
        existing.setTitle(todo.getTitle());
        existing.setDescription(todo.getDescription());
        return repo.save(existing);
    }

    @Override
    public void deleteToDo(Long id) {
        repo.deleteById(id);
    }

    @Override
    public ToDo markCompleted(Long id) {
        ToDo todo = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("ToDo not found"));
        todo.setStatus(ToDo.Status.COMPLETED);
        return repo.save(todo);
    }

    @Override
    public Page<ToDo> getAllToDos(User user, int page, int size) {
        // Correctly uses the filtered repository method
        return repo.findByUser(user, PageRequest.of(page, size));
    }

    @Override
    public Page<ToDo> searchByTitle(String title, User user, int page, int size) {
        // Correctly filters search results by user
        return repo.findByTitleContainingIgnoreCaseAndUser(title, user, PageRequest.of(page, size));
    }
}