package com.boot.todo.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.boot.todo.entities.Todo;
import com.boot.todo.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TodoControllerTests {

    private MockMvc mockMvc;

    @Mock
    private TodoService todoService;

    @InjectMocks
    private TodoController todoController;
    private Todo todo1;
    private Todo todo2;
    
    private ObjectMapper objectMapper=new ObjectMapper();
    
    AutoCloseable autoclosable;
    
  

    @BeforeEach
    public void setup() throws ParseException {
    	autoclosable= MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(todoController).build();
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
    public void testGetAllTodos() throws Exception {
    	  List<Todo> list= new ArrayList<>();
    	  list.add(todo1);
    	  list.add(todo2);
    	
        when(todoService.getAllTodoList()).thenReturn(list);

        mockMvc.perform(get("/todo")) 
               .andDo(print())
               .andExpect(status().isOk());
    }

    @Test
    public void testGetTodoById() throws Exception {
        when(todoService.getTodoListById(1)).thenReturn(todo1);

        mockMvc.perform(get("/todo/1"))
               .andExpect(status().isOk())
               .andDo(print());
    }

    @Test
    public void testAddTodo() throws Exception {
        Todo newTodo = new Todo(3, "New Todo", new Date(), "Pending");
        String jsonTodo= objectMapper.writeValueAsString(newTodo);       
        when(todoService.addItem(any(Todo.class))).thenReturn(newTodo);

        mockMvc.perform(post("/todo")
               .contentType(MediaType.APPLICATION_JSON)
               .content(jsonTodo))
               .andExpect(status().isCreated())
               .andDo(print());
    }

    @Test
    public void testUpdateTodoStatus() throws Exception {
        String newStatus = "Completed";
        when(todoService.updateStatus(newStatus, todo1.getId())).thenReturn(true);

        mockMvc.perform(put("/todo/{id}/status", todo1.getId())
               .contentType(MediaType.APPLICATION_JSON)
               .content(newStatus))
               .andExpect(status().isOk());
    }

    @Test
    public void testEditTodo() throws Exception {
    	todo1.setTitle("Updated Presentation");    	String jsonTodo= objectMapper.writeValueAsString(todo1);    
        when(todoService.editTodoItem(any(Todo.class), eq(todo1.getId()))).thenReturn(todo1);

        mockMvc.perform(put("/todo/1")
               .contentType(MediaType.APPLICATION_JSON)
               .content(jsonTodo))
               .andExpect(status().isOk())
               .andDo(print());
               
              
    }

    @Test
    public void testDeleteTodo() throws Exception {
        when(todoService.deleteTodoList(1)).thenReturn(true);

        mockMvc.perform(delete("/todo/1"))
               .andExpect(status().isNoContent())
        	   .andDo(print());
    }
}