package com.codewithvaishnavi.store.service;

//import java.util.List;

import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;


import com.codewithvaishnavi.store.model.ToDo;
import com.codewithvaishnavi.store.model.User;

public interface ToDoService {
	
    ToDo addToDo(ToDo todo);
    ToDo updateTodo(Long id, ToDo todo);
    void deleteToDo(Long id);
    ToDo markCompleted(Long id);

    Page<ToDo> getAllToDos(User user,int page, int size);
    Page<ToDo> searchByTitle(String title, User user, int page, int size);
}
