package com.unibuc.projectmanagementapp.bootstrap;

import com.unibuc.projectmanagementapp.domain.security.Role;
import com.unibuc.projectmanagementapp.domain.security.User;
import com.unibuc.projectmanagementapp.repositories.security.RoleRepository;
import com.unibuc.projectmanagementapp.repositories.security.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

@AllArgsConstructor
@Component
//@Profile("h2")
@Profile("mysql")
public class DataLoader implements CommandLineRunner {

    private final DataSource dataSource;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        loadUserData();
    }

    private void loadUserData() throws IOException, SQLException {
        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.save(
                    Role.builder()
                            .roleName("ROLE_ADMIN")
                            .build());

            Role guestRole = roleRepository.save(
                    Role.builder()
                            .roleName("ROLE_GUEST")
                            .build());

            User admin = User.builder()
                    .email("admin@pmapp.com")
                    .password(passwordEncoder.encode("12345"))
                    .role(adminRole)    // singular, datorită @Singular pe 'roles'
                    .build();

            User guest = User.builder()
                    .email("guest@pmapp.com")
                    .password(passwordEncoder.encode("12345"))
                    .role(guestRole)
                    .build();

            userRepository.save(admin);
            userRepository.save(guest);
        }

//        Resource initScript = new ClassPathResource("initial-data-h2.sql");
        Resource initScript = new ClassPathResource("initial-data.sql");
        ScriptUtils.executeSqlScript(dataSource.getConnection(), initScript);
    }
}