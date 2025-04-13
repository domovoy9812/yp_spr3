package ru.yandex.practicum.bliushtein.spr4.data.configuration;

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

    @Value("${spring.datasource.create-db-tables-on-start}")
    private boolean createDbTablesOnStart;
    @Value("${spring.datasource.drop-db-tables-on-start}")
    private boolean dropDbTablesOnStart;
    @Autowired
    private DataSource dataSource;
    @Value("classpath:db/create_db_tables.sql")
    private Resource createTablesScript;
    @Value("classpath:db/drop_db_tables.sql")
    private Resource dropTablesScript;

    @EventListener
    public void initSchema(ContextRefreshedEvent event) {
        if (createDbTablesOnStart) {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            if (dropDbTablesOnStart) {
                populator.addScript(dropTablesScript);
            }
            populator.addScript(createTablesScript);
            populator.execute(dataSource);
        }
    }
}
