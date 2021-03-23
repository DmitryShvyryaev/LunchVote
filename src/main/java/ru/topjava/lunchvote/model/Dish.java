package ru.topjava.lunchvote.model;

import ch.qos.logback.core.joran.action.IncludeAction;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import org.hibernate.annotations.Cache;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "dishes", uniqueConstraints = {@UniqueConstraint(columnNames = {"date", "restaurant_id"}, name = "dishes_unique_rest_date_idx")} )
public class Dish extends AbstractNamedEntity {

    @Column(name = "price", nullable = false)
    @NotNull
    private Double price;

    @Column(name = "date", nullable = false)
    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    public Dish() {
    }

    public Dish(String name, Double price, LocalDate date) {
        this.name = name;
        this.price = price;
        this.date = date;
    }

    public Dish(Long id, String name, Double price, LocalDate date) {
        super(id, name);
        this.price = price;
        this.date = date;
    }

    public Dish(Long id, String name, Double price, LocalDate date, Restaurant restaurant) {
        super(id, name);
        this.price = price;
        this.date = date;
    }

    public Dish(Dish dish) {
        this(dish.id, dish.name, dish.getPrice(), dish.date);
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", date=" + date +
                '}';
    }
}
