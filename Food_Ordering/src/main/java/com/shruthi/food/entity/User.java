package com.shruthi.food.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shruthi.food.entity.Address;
import com.shruthi.food.dto.RestaurantDTO;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String fullName;
	
	private String email;
	
	private String password;
	
    @Enumerated(EnumType.STRING)
    private USER_ROLE role;
	
	@JsonIgnore
	@OneToMany(cascade= CascadeType.ALL, mappedBy="customer")
	private List<Order> orders=new ArrayList<>();

	 @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	    @JoinColumn(name = "user_id")
	    private List<UserFavoriteRestaurant> favorites = new ArrayList<>();
	 
	 @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
	    private List<Address> addresses = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public USER_ROLE getRole() {
		return role;
	}

	public void setRole(USER_ROLE role) {
		this.role = role;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public List<UserFavoriteRestaurant> getFavorites() {
		return favorites;
	}

	public void setFavorites(List<UserFavoriteRestaurant> favorites) {
		this.favorites = favorites;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	

}
