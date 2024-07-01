package com.shruthi.food.entity;
import java.util.List;

import javax.persistence.*;


@Entity
@Table(name = "user_favorite_restaurants")
public class UserFavoriteRestaurant {
	 @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;

    @ElementCollection
    @Column(length = 1000)
    private List<String> images;

    private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    
    
}