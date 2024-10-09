package GreenSpark.greenspark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GreensparkApplication {
	public static void main(String[] args) {
		SpringApplication.run(GreensparkApplication.class, args);
	}

}
