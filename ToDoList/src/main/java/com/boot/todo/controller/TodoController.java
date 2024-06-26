package com.boot.todo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.boot.todo.entities.Todo;
import com.boot.todo.service.TodoService;


@RestController
public class TodoController {

	@Autowired
	private TodoService todoservice;
	
	@GetMapping("/todo")
	public ResponseEntity<List<Todo>> getAllTodos(){
		List<Todo>list=todoservice.getAllTodoList();
		if(!list.isEmpty()) {
			return ResponseEntity.ok(list);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@GetMapping("/todo/{id}")
	public ResponseEntity<Todo> getTodosById(@PathVariable("id") int id){
		Todo todo=todoservice.getTodoListById(id);
		if(todo!=null) {
			return ResponseEntity.ok(todo);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	
	@PostMapping("/todo")
    public ResponseEntity<Todo> addTodo(@RequestBody Todo todo){
        Todo newTodo = todoservice.addItem(todo);
        if (newTodo != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(newTodo);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
	
	@PutMapping("/todo/{id}/status")
	public ResponseEntity<Todo> updateTodoStatus(@RequestBody String newStatus,@PathVariable("id") int id){
		boolean updated=todoservice.updateStatus(newStatus,id);
		if(updated) {
			return ResponseEntity.ok().build();
			}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@PutMapping("/todo/{id}")
	public ResponseEntity<Todo> editTodo(@RequestBody Todo updatedTodo,@PathVariable("id") int id){
		Todo updateditem=todoservice.editTodoItem(updatedTodo,id);
		if(updateditem!=null) {
			return ResponseEntity.ok(updateditem);
			}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@DeleteMapping("/todo/{id}")
	public ResponseEntity<Todo> deleteTodo(@PathVariable("id") int id){
		boolean deleted=todoservice.deleteTodoList(id);
		if(deleted) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
