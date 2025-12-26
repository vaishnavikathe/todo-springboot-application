package com.codewithvaishnavi.store.repository;

//import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.codewithvaishnavi.store.model.ToDo;
import com.codewithvaishnavi.store.model.User;

public interface ToDoRepository extends JpaRepository<ToDo, Long>{

	//List<ToDo> findByTitleContainingIgnoreCase(String title);
 
	//Fetch only ToDos belonging to a specific user
	Page<ToDo> findByUser(User user, Pageable pageable);
	
	//Search ToDos only within user's own login
	Page<ToDo> findByTitleContainingIgnoreCaseAndUser(String title, User user, Pageable pageable);
}
