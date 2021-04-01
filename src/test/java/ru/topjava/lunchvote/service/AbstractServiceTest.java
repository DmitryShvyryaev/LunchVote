package ru.topjava.lunchvote.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.topjava.lunchvote.util.TimingExtension;

@SpringJUnitConfig(locations = {"classpath:spring/spring-config.xml"})
@Sql(scripts = "classpath:db/populate.sql", config = @SqlConfig(encoding = "UTF-8"))
@ExtendWith(TimingExtension.class)
public abstract class AbstractServiceTest {

    @Autowired
    protected CacheManager cacheManager;
}
