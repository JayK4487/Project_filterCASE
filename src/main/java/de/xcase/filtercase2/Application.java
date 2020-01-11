package de.xcase.filtercase2;

import de.xcase.filtercase2.views.ErrorView;
import de.xcase.filtercase2.views.LoginView;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = ErrorView.class)
@ComponentScan(basePackageClasses = LoginView.class)
@EnableWebSecurity
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
