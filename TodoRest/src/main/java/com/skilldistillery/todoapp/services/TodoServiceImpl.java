package com.skilldistillery.todoapp.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skilldistillery.todoapp.entities.Todo;
import com.skilldistillery.todoapp.entities.User;
import com.skilldistillery.todoapp.repositories.TodoRepository;
import com.skilldistillery.todoapp.repositories.UserRepository;

@Service
public class TodoServiceImpl implements TodoService {

	@Autowired
	private TodoRepository repoTodo;
	
	@Autowired
	private UserRepository repoUser;
	
	@Override
	public Set<Todo> index(String username) {
		Set<Todo> todo = new HashSet<Todo>(repoTodo.findAll());
		return todo;
	}

	@Override
	public Todo show(String username, Integer tid) {
		List<User> users = repoUser.findAll();
		User newuser = null;
		for (User user : users) {
			if(user.getUsername().equals(username) ) {
				newuser = user;
			}
		}
		if( newuser != null) {
			for (Todo todo : newuser.getTodos()) {
				if(todo.getId() == tid ) {
					return todo;
				}
			}
		}

		return null;
	}

	@Override
	public Todo create(String username, Todo todo) {
		if(todo.getUser() == null) {
			todo.setUser(new User());
			todo.getUser().setUsername(username);
			todo.getUser().setId(1);
		}

		return repoTodo.saveAndFlush(todo);
	}

	@Override
	public Todo update(String username, int tid, Todo todo) {
	       Optional<Todo> opt = repoTodo.findById(tid);
	        if (opt.isPresent()) {
	        	Todo managed = opt.get();
	            managed.setTask(todo.getTask());
	            managed.setDescription(todo.getDescription());
	            managed.setCompleted(todo.getCompleted());
	            managed.setDueDate(todo.getDueDate());
	            managed.setCompleteDate(todo.getCompleteDate());
	            repoTodo.saveAndFlush(managed);
	            return managed;
	        }
	        return null;
	}

	@Override
	public boolean destroy(String username, int tid) {
		repoTodo.deleteById(tid);
		if (repoTodo.existsById(tid)) {
			return false;
		} else {
			return true;
		}
	}

}
