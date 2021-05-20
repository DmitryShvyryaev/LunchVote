package ru.topjava.lunchvote.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import ru.topjava.lunchvote.exception.ErrorType;
import ru.topjava.lunchvote.model.Dish;
import ru.topjava.lunchvote.model.User;
import ru.topjava.lunchvote.service.DishService;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringJUnitWebConfig(locations = {"classpath:spring/spring-config.xml",
        "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-test.xml"})
@Sql(scripts = "classpath:db/populate.sql", config = @SqlConfig(encoding = "UTF-8"))
public abstract class AbstractControllerTest {
    private static final Locale RU_LOCALE = new Locale("ru");

    private static final CharacterEncodingFilter CHARACTER_ENCODING_FILTER = new CharacterEncodingFilter();

    static {
        CHARACTER_ENCODING_FILTER.setEncoding("UTF-8");
        CHARACTER_ENCODING_FILTER.setForceEncoding(true);
    }

    @Autowired
    protected DishService dishService;

    protected MockMvc mockMvc;
    @Autowired
    protected MessageSourceAccessor messageSourceAccessor;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @PostConstruct
    private void postConstruct() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(CHARACTER_ENCODING_FILTER)
                .apply(springSecurity())
                .build();
    }

    protected ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }

    protected List<Dish> populateMenu(long restaurantId, int countOfDishes) {
        LocalDate today = LocalDate.now();
        List<Dish> result = new ArrayList<>();
        for (int i = 0; i < countOfDishes; i++) {
            Dish dish = new Dish("name" + restaurantId + (i * 10), restaurantId + (i * 101L), today);
            result.add(dishService.create(restaurantId, dish));
        }
        return result;
    }

    protected RequestPostProcessor userHttpBasic(User user) {
        return SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword());
    }

    public ResultMatcher errorType(ErrorType type) {
        return jsonPath("$.type").value(getMessage(type.getErrorCode()));
    }

    public ResultMatcher detailMessage(String code) {
        return jsonPath("$.details").value(getMessage(code));
    }

    private String getMessage(String code) {
        return messageSourceAccessor.getMessage(code, RU_LOCALE);
    }
}