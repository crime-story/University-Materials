package com.unibuc.task.bootstrap;

import com.unibuc.task.domain.security.Role;
import com.unibuc.task.domain.security.User;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

@AllArgsConstructor
@Component
@Profile("mysql")
public class DataLoader implements CommandLineRunner {

    private final DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        loadUserData();
    }

    private void loadUserData() throws IOException, SQLException {

        Resource initScript = new ClassPathResource("initial-data.sql");
        ScriptUtils.executeSqlScript(dataSource.getConnection(), initScript);
    }
}