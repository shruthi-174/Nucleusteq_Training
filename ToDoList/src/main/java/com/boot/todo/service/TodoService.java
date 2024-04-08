package com.boot.todo.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.todo.entities.Todo;
import com.boot.todo.repository.TodoRepository;


@Service
public class TodoService {
	
	@Autowired
	private TodoRepository trepo;

	public List<Todo> getAllTodoList(){
		List<Todo> list=new ArrayList<>();
		list=trepo.findAll();
		return list;
	}
	
	public Todo getTodoListById(int id) {
		return trepo.findById(id).orElse(null);
	}
	
	public Todo addItem(Todo todo) {
		return trepo.save(todo);
	}
	
	public boolean updateStatus(String newStatus,int id) {
		Todo todo=trepo.findById(id).orElse(null);
		if(todo!=null) {
			todo.setStatus(newStatus);
			trepo.save(todo);
			return true;
		}
		return false;
	}
	
	public Todo editTodoItem(Todo updatedTodo,int id) {
		Todo todo=trepo.findById(id).orElse(null);
		if(todo!=null) {
			todo.setTitle(updatedTodo.getTitle());
			todo.setDate(updatedTodo.getDate());
			return trepo.save(todo);
		}
		return null;
	}

	public boolean deleteTodoList(int id){
		trepo.deleteById(id);
		if(trepo.findById(id).isPresent())
			return true;
		return false;
	}
}
