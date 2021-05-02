package ru.topjava.lunchvote.testdata;

import ru.topjava.lunchvote.model.Dish;
import ru.topjava.lunchvote.util.Matcher;

import java.util.List;

import static ru.topjava.lunchvote.testdata.DateTestData.FIRST_DAY;
import static ru.topjava.lunchvote.testdata.DateTestData.SECOND_DAY;

public class DishTestData {

    public static final long START_SEQ_DISH = 100008L;

    public static final Matcher<Dish> DISH_MATCHER = Matcher.getComparator(Dish.class, "restaurant");

    public static final Dish tanukiFirstDayDish1 = new Dish(START_SEQ_DISH, "Мисо-суп", 21050L, FIRST_DAY);
    public static final Dish tanukiFirstDayDish2 = new Dish(START_SEQ_DISH + 1, "Калифорния", 42035L, FIRST_DAY);
    public static final Dish tanukiSecondDayDish1 = new Dish(START_SEQ_DISH + 2, "Суп с угрем", 30099L, SECOND_DAY);
    public static final Dish tanukiSecondDayDish2 = new Dish(START_SEQ_DISH + 3, "Филадельфия", 39078L, SECOND_DAY);

    public static final Dish macFirstDayDish1 = new Dish(START_SEQ_DISH + 4, "Картофель Фри", 5066L, FIRST_DAY);
    public static final Dish macFirstDayDish2 = new Dish(START_SEQ_DISH + 5, "Биг Мак", 17005L, FIRST_DAY);
    public static final Dish macSecondDayDish1 = new Dish(START_SEQ_DISH + 6, "Наггетсы", 10280L, SECOND_DAY);
    public static final Dish macSecondDayDish2 = new Dish(START_SEQ_DISH + 7, "Биг Тейсти", 19999L, SECOND_DAY);

    public static final List<Dish> tanukiFirstDay = List.of(tanukiFirstDayDish1, tanukiFirstDayDish2);
    public static final List<Dish> tanukiSecondDay = List.of(tanukiSecondDayDish1, tanukiSecondDayDish2);

    public static final List<Dish> macFirstDay = List.of(macFirstDayDish1, macFirstDayDish2);
    public static final List<Dish> macSecondDay = List.of(macSecondDayDish1, macSecondDayDish2);

    private DishTestData() {
    }

    public static Dish getCreated() {
        return new Dish(null, "Пицца", 60054L, FIRST_DAY);
    }

    public static Dish getUpdated() {
        Dish dish = new Dish(tanukiFirstDayDish1);
        dish.setDate(SECOND_DAY);
        dish.setPrice(100000L);
        return dish;
    }
}
