package com.shruthi.food.dto;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;

@Embeddable
public class RestaurantDTO {
    private Long id;
    private String title;

    @ElementCollection
    @Column(length = 1000)
    private List<String> images;

    private String description;

    // Getters and Setters
}
