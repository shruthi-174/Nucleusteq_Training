package com.boot.todo.repository;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.boot.todo.entities.Todo;
import com.boot.todo.repository.TodoRepository;

@DataJpaTest
class TodoRepositoryTests {

	@Autowired
	private EntityManager entityManager;
	
    @Autowired
    private TodoRepository todoRepository;

    private Todo todo1, todo2;
    private Todo savedTodo1, savedTodo2;

    @BeforeEach
    void setUp() throws ParseException {
        // Create the table if it doesn't exist
        entityManager.createNativeQuery("CREATE TABLE IF NOT EXISTS todo (id INT AUTO_INCREMENT PRIMARY KEY, title VARCHAR(255), date DATE, status VARCHAR(255))").executeUpdate();

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
        // Drop the table after each test
        entityManager.createNativeQuery("DROP TABLE todo").executeUpdate();
    }


    @Test
    void testFindById() {
        Todo savedTodo = todoRepository.save(todo1);
        Optional<Todo> foundTodo = todoRepository.findById(savedTodo.getId());
        assertTrue(foundTodo.isPresent());
        assertEquals(savedTodo, foundTodo.get());
    }


    @Test
    void testFindAll() {
        List<Todo> todoList = todoRepository.findAll();
        assertEquals(2, todoList.size());
        assertTrue(todoList.contains(savedTodo1));
        assertTrue(todoList.contains(savedTodo2));
    }

    @Test
    void testDeleteById() {
        Todo savedTodo = todoRepository.save(todo1);
        todoRepository.deleteById(savedTodo.getId());
        Optional<Todo> foundTodo = todoRepository.findById(savedTodo.getId());
        assertFalse(foundTodo.isPresent());
    }
    @Test
    void testAddItem() {
        Todo newTodo = new Todo(1,"New Task", new Date(), "Pending");
        Todo savedTodo = todoRepository.save(newTodo);

        assertNotNull(savedTodo);
        assertNotNull(savedTodo.getId());
        assertEquals(newTodo.getTitle(), savedTodo.getTitle());
        assertEquals(newTodo.getDate(), savedTodo.getDate());
        assertEquals(newTodo.getStatus(), savedTodo.getStatus());
    }

    @Test
    void testUpdateStatus() {
        Todo savedTodo = todoRepository.save(todo1);
        String newStatus = "Completed";
        savedTodo.setStatus(newStatus);
        Todo updatedTodo = todoRepository.save(savedTodo);

        assertNotNull(updatedTodo);
        assertEquals(newStatus, updatedTodo.getStatus());
    }

    @Test
    void testEditTodoItem() {
        Todo savedTodo = todoRepository.save(todo1);
        String newTitle = "Updated Task";
        Date newDate = new Date();
        savedTodo.setTitle(newTitle);
        savedTodo.setDate(newDate);
        Todo updatedTodo = todoRepository.save(savedTodo);

        assertNotNull(updatedTodo);
        assertEquals(newTitle, updatedTodo.getTitle());
        assertEquals(newDate, updatedTodo.getDate());
    }
    

}