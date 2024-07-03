package com.shruthi.food.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shruthi.food.entity.Cart;


public interface CartRepository extends JpaRepository<Cart,Long>{

}
