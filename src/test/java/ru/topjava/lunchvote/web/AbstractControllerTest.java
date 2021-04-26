package ru.topjava.lunchvote.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import ru.topjava.lunchvote.model.Dish;
import ru.topjava.lunchvote.service.DishService;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringJUnitWebConfig(locations = {"classpath:spring/spring-config.xml",
        "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-test.xml"})
@Sql(scripts = "classpath:db/populate.sql", config = @SqlConfig(encoding = "UTF-8"))
public abstract class AbstractControllerTest {

    private static final CharacterEncodingFilter CHARACTER_ENCODING_FILTER = new CharacterEncodingFilter();

    static {
        CHARACTER_ENCODING_FILTER.setEncoding("UTF-8");
        CHARACTER_ENCODING_FILTER.setForceEncoding(true);
    }

    @Autowired
    protected DishService dishService;

    @Autowired
    protected CacheManager cacheManager;
    protected MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @PostConstruct
    private void postConstruct() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilters(CHARACTER_ENCODING_FILTER).build();
    }

    protected ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }

    protected List<Dish> populateMenu(long restaurantId, int countOfDishes) {
        LocalDate today = LocalDate.now();
        List<Dish> result = new ArrayList<>();
        for (int i = 0; i < countOfDishes; i++) {
            Dish dish = new Dish("name" + restaurantId + (i * 10), restaurantId + (i * 100) + 0.1, today);
            result.add(dishService.create(restaurantId, dish));
        }
        return result;
    }
}