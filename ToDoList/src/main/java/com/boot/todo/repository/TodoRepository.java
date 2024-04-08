package com.boot.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boot.todo.entities.Todo;

public interface TodoRepository extends JpaRepository<Todo,Integer>{

}
