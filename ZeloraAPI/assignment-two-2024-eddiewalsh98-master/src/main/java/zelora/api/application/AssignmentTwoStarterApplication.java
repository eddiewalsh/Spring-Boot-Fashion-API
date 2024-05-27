package zelora.api.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@ComponentScan({"zelora.api.services", "zelora.api.controller", "zelora.api.security"})
@EntityScan("zelora.api.model")
@EnableJpaRepositories("zelora.api.repository")
public class AssignmentTwoStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssignmentTwoStarterApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer customConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
                configurer.defaultContentType(MediaType.APPLICATION_JSON);
            }
        };
    }
}
