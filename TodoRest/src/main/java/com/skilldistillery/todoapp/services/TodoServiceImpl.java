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
		Set<Todo> todos= null;
		User u = repoUser.findByUsername(username);
		todos = new HashSet<Todo>(repoTodo.findByUser_Username(username));
		return todos;
	}

    @Override
    public Todo show(String username, Integer tid) {
        Todo t = null;
        Optional<Todo> opt = repoTodo.findById(tid);
        if (opt.isPresent()) {
            if (opt.get().getUser().getUsername().equals(username)) {
                t = opt.get();
            }
        }
        return t;
    }
	


	@Override
	public Todo create(String username, Todo todo) {
		User u = repoUser.findByUsername(username);
		  if (u != null) {
			todo.setUser(u);
			todo.getUser().setUsername(u.getUsername());
			todo.getUser().setId(u.getId());
		}

		return repoTodo.saveAndFlush(todo);
	}

	@Override
	public Todo update(String username, int tid, Todo todo) {
	       Optional<Todo> opt = repoTodo.findById(tid);
	        if (opt.isPresent()) {
	        	Todo managed = opt.get();
	            if (managed.getUser().getUsername().equals(username)) {
	                managed.setTask(todo.getTask());
	                managed.setDescription(todo.getDescription());
	                managed.setCompleted(todo.getCompleted());
	                managed.setDueDate(todo.getDueDate());
	                managed.setCompleteDate(todo.getCompleteDate());
	                repoTodo.saveAndFlush(managed);
	                return managed;
	            }
	        }
	        return null;
	}

    @Override
    public boolean destroy(String username, int tid) {
        Optional<Todo> opt = repoTodo.findById(tid);
        if (opt.isPresent()) {
            if (opt.get().getUser().getUsername().equals(username)) {
                repoTodo.deleteById(tid);
                return true;
            }
        }
        return false;
    }

}
