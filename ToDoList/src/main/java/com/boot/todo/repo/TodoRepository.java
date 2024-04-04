package com.boot.todo.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boot.todo.enitites.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo,Integer>{

}
