package com.skilldistillery.todoapp.controllers;

import java.security.Principal;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.skilldistillery.todoapp.entities.Todo;
import com.skilldistillery.todoapp.services.TodoService;

@RestController
@RequestMapping("api")
@CrossOrigin({ "*", "http://localhost:4202" })
public class TodoController {

	@Autowired
	private TodoService service;

//	private String username ="shaun";
	
	
	//  GET todos
	@GetMapping("todos")
	public Set<Todo> index(HttpServletRequest req, HttpServletResponse res,
			Principal principal) {
		return service.index(principal.getName());
	}

	@GetMapping("todos/{tid}")
	public Todo show(HttpServletRequest req, HttpServletResponse res,
			@PathVariable("tid") Integer tid,
			Principal principal) {
		try {
			Todo todo = service.show(principal.getName(),tid);
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
			@RequestBody Todo todo,
			Principal principal) {
		try {
			service.create(principal.getName(),todo);
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
			@RequestBody Todo todo,
			Principal principal) {
		todo = service.update(principal.getName(),tid, todo);
	        if (todo == null) {
	            res.setStatus(404);
	        }
	        return todo;
		
	}

	@DeleteMapping("todos/{tid}")
	public Boolean destroy(
			HttpServletRequest req,
			HttpServletResponse res, 
			@PathVariable("tid") Integer tid,
			Principal principal) {
		try {
			if (service.show(principal.getName(), tid) == null) {
				res.setStatus(404);
				return false;
			} else {
				service.destroy(principal.getName(), tid);
				res.setStatus(204);
				return true;
			}

		} catch (Exception e) {
			res.setStatus(409);
			return false;
		}
		
	}

}
