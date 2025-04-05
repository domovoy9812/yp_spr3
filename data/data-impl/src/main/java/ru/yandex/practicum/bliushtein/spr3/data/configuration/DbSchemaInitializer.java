package ru.yandex.practicum.bliushtein.spr3.data.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DbSchemaInitializer {

    @Value("${spring.datasource.init_on_start}")
    private boolean enabled;
    @Autowired
    private DataSource dataSource;
    @Value("classpath:init_db/schema.sql")
    private Resource initScript;

    @EventListener
    public void initSchema(ContextRefreshedEvent event) {
        if (enabled) {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.addScript(initScript);
            populator.execute(dataSource);
        }
    }
}
