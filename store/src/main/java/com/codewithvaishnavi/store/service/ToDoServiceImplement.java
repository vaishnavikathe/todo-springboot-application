package com.codewithvaishnavi.store.service;

//import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.codewithvaishnavi.store.model.ToDo;
import com.codewithvaishnavi.store.model.User;
import com.codewithvaishnavi.store.repository.ToDoRepository;

@Service
public class ToDoServiceImplement implements ToDoService {

	@Autowired
	private final ToDoRepository repo;
	
	public ToDoServiceImplement(ToDoRepository repo) {
		this.repo = repo;
	}
	
	@Override
	public ToDo addToDo(ToDo todo) {
		todo.setStatus(ToDo.Status.PENDING);
		return repo.save(todo);
	}

	//@Override
	//public List<ToDo> getAllToDos() {
		// TODO Auto-generated method stub
		//return repo.findAll();
	//}

	@Override
	public ToDo updateTodo(Long id, ToDo todo) {
		// TODO Auto-generated method stub
		ToDo existing = repo.findById(id).orElseThrow(() -> new RuntimeException("ToDo not found"));
		existing.setTitle(todo.getTitle());
		existing.setDescription(todo.getDescription());
		return repo.save(existing);
	}

	@Override
	public void deleteToDo(Long id) {
		// TODO Auto-generated method stub
		repo.deleteById(id);
	}

	@Override
	public ToDo markCompleted(Long id) {
		// TODO Auto-generated method stub
		
		ToDo todo = repo.findById(id).orElseThrow();
		todo.setStatus(ToDo.Status.COMPLETED);
		return repo.save(todo);
	}

	//@Override
	//public List<ToDo> searchByTitle(String title) {
		// TODO Auto-generated method stub
		
		//return repo.findByTitleContainingIgnoreCase(title);
	//}
	
	//@Override
	//public List<ToDo> findAll(org.springframework.data.domain.Pageable pageable) {
		// TODO Auto-generated method stub
		//return null;
	//}

	/*
	@Override
	public Page<ToDo> getAllToDos(int page, int size) {
		// TODO Auto-generated method stub
		 return repo.findAll(PageRequest.of(page, size));
	}

	@Override
	public Page<ToDo> searchByTitle(String title, int page, int size) {
		// TODO Auto-generated method stub
		return repo.findByTitleContainingIgnoreCase(
                title,
                , PageRequest.of(page, size)
        );
	}
	*/

	@Override
	public Page<ToDo> getAllToDos(User user, int page, int size) {
		// TODO Auto-generated method stub
		return repo.findByUser(user, PageRequest.of(page, size));
	}

	@Override
	public Page<ToDo> searchByTitle(String title, User user, int page, int size) {
		// TODO Auto-generated method stub
		return repo.findByTitleContainingIgnoreCase(title, user, PageRequest.of(page, size));
	}

}
