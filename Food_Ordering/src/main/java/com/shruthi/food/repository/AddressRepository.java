package com.shruthi.food.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shruthi.food.entity.Address;

public interface AddressRepository extends JpaRepository<Address,Long>{

}
