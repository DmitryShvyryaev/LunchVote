package ru.topjava.lunchvote.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "restaurants", uniqueConstraints = {@UniqueConstraint(columnNames = "name", name = "restaurants_name_idx")})
public class Restaurant extends AbstractNamedEntity {

    @Column(name = "description", nullable = false)
    @NotBlank
    @Size(min = 10, max = 300)
    private String description;

    public Restaurant() {
    }

    public Restaurant(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Restaurant(Long id, String name, String description) {
        super(id, name);
        this.description = description;
    }

    public Restaurant(Restaurant restaurant) {
        this(restaurant.id, restaurant.getName(), restaurant.getDescription());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
