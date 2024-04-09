package com.boot.todo.repository;
import com.boot.todo.entities.Todo;
import com.boot.todo.repository.TodoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TodoRepositoryTests {

    @Autowired
    private TodoRepository todoRepository;

    private Todo todo1;
    private Todo todo2;
    private Todo savedTodo1;
    private Todo savedTodo2;

    @BeforeEach
    void setUp() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = dateFormat.parse("2024-04-05");
        Date date2 = dateFormat.parse("2024-03-30");
        todo1 = new Todo(1, "Project Presentation", date1, "Pending");
        todo2 = new Todo(2, "Exercise", date2, "Completed");
        savedTodo1 = todoRepository.save(todo1);
        savedTodo2 = todoRepository.save(todo2);
    }

    @AfterEach
    void tearDown() {
        todoRepository.deleteAll();
    }

    @Test
    void testGetTodoById() {
        Todo todo = todoRepository.findById(savedTodo1.getId()).orElse(null);
        assertThat(todo).isNotNull();
        assertThat(todo).isEqualTo(savedTodo1);
    }
    
    @Test
    void testGetAllTodoList() {
    	List<Todo> ls=todoRepository.findAll();
    	assertThat(ls.size()).isEqualTo(2);
    	assertThat(ls).contains(savedTodo1,savedTodo2);
    }
    
    @Test
    void testAddItem() {
    	Todo todo=new Todo(121,"Yoga",new Date(),"Pending");
    	todoRepository.save(todo);
    	
    	assertThat(todo).isNotNull();
    	assertThat(todo.getId()).isGreaterThan(0);
    }
    
  @Test
  void testUpdateStatus() {
  	savedTodo1.setStatus("Completed");
  	Todo updatedTodo=todoRepository.save(savedTodo1);
  	assertThat(updatedTodo.getStatus()).isEqualTo("Completed");
  }
    
    @Test
    void testEditTodoItem() {
    	savedTodo1.setTitle("PP");
    	Todo updatedTodo=todoRepository.save(savedTodo1);
    	assertThat(updatedTodo.getTitle()).isEqualTo("PP");
    }
    
  @Test
  void testDeleteTodoList() {
  	todoRepository.deleteById(savedTodo1.getId());
  	Todo deletedTodo=todoRepository.findById(savedTodo1.getId()).orElse(null);
  	assertThat(deletedTodo).isNull();
  }
}
