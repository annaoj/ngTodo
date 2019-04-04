package com.skilldistillery.todoapp.controllers;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skilldistillery.todoapp.entities.Todo;
import com.skilldistillery.todoapp.services.TodoService;

@RestController
@RequestMapping("api")
@CrossOrigin({ "*", "http://localhost:4202" })
public class TodoController {

	@Autowired
	private TodoService service;

	private String username ="shaun";
	
	//  GET todos
	@GetMapping("todos")
	public Set<Todo> index(HttpServletRequest req, HttpServletResponse res) {
		return service.index(username);
	}

	@GetMapping("todos/{tid}")
	public Todo show(HttpServletRequest req, HttpServletResponse res,
			@PathVariable("tid") Integer tid) {
		try {
			Todo todo = service.show(username,tid);
			if (todo == null) {
				res.setStatus(404);
			} else {
				StringBuffer url = req.getRequestURL();
				url.append("/");
				url.append(tid);
				res.setHeader("Location", url.toString());

				res.setStatus(201);
			}

			return todo;
		} catch (Exception e) {
			res.setStatus(500);
			return null;
		}
	}

	@PostMapping("todos")
	public Todo create(HttpServletRequest req, HttpServletResponse res, 
			@RequestBody Todo todo) {
		try {
			service.create(username,todo);
			StringBuffer url = req.getRequestURL();
			System.out.println("PostController" + url.toString());
			url.append("/");
			url.append(todo.getId());
			res.setHeader("Location", url.toString());
			res.setStatus(201);
			return todo;
		} catch (Exception e) {
			res.setStatus(400);
			e.printStackTrace();
			return null;
		}
	}

	@PutMapping("todos/{tid}") 
	public Todo update(
			HttpServletRequest req, 
			HttpServletResponse res,
			@PathVariable("tid") Integer tid,
			@RequestBody Todo todo) {
		todo = service.update(username,tid, todo);
	        if (todo == null) {
	            res.setStatus(404);
	        }
	        return todo;
		
	}

	@DeleteMapping("todos/{tid}")
	public Boolean destroy(
			HttpServletRequest req,
			HttpServletResponse res, 
			@PathVariable("tid") Integer tid) {
		try {
			if (service.show(username, tid) == null) {
				res.setStatus(404);
				return false;
			} else {
				service.destroy(username, tid);
				res.setStatus(204);
				return true;
			}

		} catch (Exception e) {
			res.setStatus(409);
			return false;
		}
		
	}

}
