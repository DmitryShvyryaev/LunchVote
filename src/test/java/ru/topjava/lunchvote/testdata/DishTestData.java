package ru.topjava.lunchvote.testdata;

import ru.topjava.lunchvote.model.Dish;
import ru.topjava.lunchvote.util.Matcher;

import java.util.List;

import static ru.topjava.lunchvote.testdata.DateTestData.*;
import static ru.topjava.lunchvote.testdata.RestaurantTestData.rest2;

public class DishTestData {

    private DishTestData() {
    }

    public static final long START_SEQ_DISH = 100008L;

    public static final Matcher<Dish> DISH_MATCHER = Matcher.getComparator("restaurant");

    public static final Dish tanukiFirstDayDish1 = new Dish(START_SEQ_DISH, "Мисо-суп", 210.50, FIRST_DAY);
    public static final Dish tanukiFirstDayDish2 = new Dish(START_SEQ_DISH + 1, "Калифорния", 420.35, FIRST_DAY);

    public static final Dish tanukiSecondDayDish1 = new Dish(START_SEQ_DISH + 2, "Суп с угрем", 300.99, SECOND_DAY);
    public static final Dish tanukiSecondDayDish2 = new Dish(START_SEQ_DISH + 3, "Филадельфия", 390.78, SECOND_DAY);

    public static final Dish macFirstDayDish1 = new Dish(START_SEQ_DISH + 4, "Картофель Фри", 50.66, FIRST_DAY);
    public static final Dish macFirstDayDish2 = new Dish(START_SEQ_DISH + 5, "Биг Мак", 170.05, FIRST_DAY);

    public static final Dish macSecondDayDish1 = new Dish(START_SEQ_DISH + 6, "Наггетсы", 102.80, SECOND_DAY);
    public static final Dish macSecondDayDish2 = new Dish(START_SEQ_DISH + 7,"Биг Тейсти", 199.99, SECOND_DAY);

    public static final List<Dish> tanukiFirstDay = List.of(tanukiFirstDayDish1, tanukiFirstDayDish2);
    public static final List<Dish> tanukiSecondDay = List.of(tanukiSecondDayDish1, tanukiSecondDayDish2);

    public static final List<Dish> macFirstDay = List.of(macFirstDayDish1, macFirstDayDish2);
    public static final List<Dish> macSecondDay = List.of(macSecondDayDish1, macSecondDayDish2);

    public static Dish getCreated() {
        return new Dish(null, "Пицца", 600.54,  FIRST_DAY);
    }

    public static Dish getUpdated() {
        Dish dish = new Dish(tanukiFirstDayDish1);
        dish.setDate(SECOND_DAY);
        dish.setRestaurant(rest2);
        dish.setPrice(1000.0);
        return dish;
    }
}
