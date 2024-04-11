package com.boot.todo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.boot.todo.entities.Todo;
import com.boot.todo.repository.TodoRepository;
import com.boot.todo.service.TodoService;

public class TodoServiceTests {
	
	@Mock
	private TodoRepository todorepository;
	
	@InjectMocks
	private TodoService todoservice;
	private  Todo todo1;
	private Todo todo2;
	
	AutoCloseable autoclosable;
	
	@BeforeEach
	public void setUp() throws ParseException {
		autoclosable=MockitoAnnotations.openMocks(this);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = dateFormat.parse("2024-04-05");
        Date date2 = dateFormat.parse("2024-03-30");
         todo1 = new Todo(1, "Project Presentation", date1, "Pending");
         todo2 = new Todo(2, "Exercise", date2, "Completed");
        
	}
	
	@AfterEach
	public void tearDown() throws Exception {
		autoclosable.close();
	}
	

	@Test
	public void testGetAllTodoList() {
		List<Todo> list=new ArrayList<>();
		list.add(todo1);
		list.add(todo2);
		when(todorepository.findAll()).thenReturn(list);
		
		List<Todo> result=todoservice.getAllTodoList();
		assertThat(result.size()).isEqualTo(2);
		assertEquals("Project Presentation",result.get(0).getTitle());
	}
	
	@Test
	public void testGetTodoListById() {
	 	when(todorepository.findById(1)).thenReturn(Optional.ofNullable(todo1));
		
		Todo result=todoservice.getTodoListById(1);
		assertThat(result).isNotNull();
		assertThat(result).isEqualTo(todo1);
	}
	
	@Test
	public void testAddItem() {
		Todo newTodo=new Todo(11,"Yoga",new Date(),"Completed");
		
	 	when(todorepository.save(newTodo)).thenReturn(newTodo);
		
		Todo result=todoservice.addItem(newTodo);
		assertThat(result).isNotNull();
		assertThat(result.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testUpdateStatus() {
		todo1.setStatus("Completed");
	 	when(todorepository.findById(1)).thenReturn(Optional.ofNullable(todo1));
	 	when(todorepository.save(todo1)).thenReturn(todo1);
		
		boolean result=todoservice.updateStatus("Completed",1);
		assertThat(result).isTrue();
		assertThat(todo1.getStatus()).isEqualTo("Completed");
		verify(todorepository,times(1)).save(todo1);
	}
	
	@Test
	public void testEditTodoItem() {
		todo1.setTitle("Updated");	
	 	when(todorepository.findById(1)).thenReturn(Optional.ofNullable(todo1));
	 	when(todorepository.save(todo1)).thenReturn(todo1);
		
		Todo result=todoservice.editTodoItem(todo1,1);
		assertThat(result.getTitle()).isEqualTo("Updated");
	}
	
	@Test
	public void testDeleteTodoList() {
	 	when(todorepository.findById(1)).thenReturn(Optional.ofNullable(todo1));
	 	doNothing().when(todorepository).deleteById(1);
		
		boolean result=todoservice.deleteTodoList(1);
		assertThat(result).isTrue();
		verify(todorepository,times(1)).deleteById(1);
	}
		
}
