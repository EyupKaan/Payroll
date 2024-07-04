package org.eyupkaan.payrollnonrest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository){
        return args -> {
          log.info("Preloading " + repository.save(new Employee("Eyüp Kaan Özteriş", "Java Spring Developer")));
          log.info("Preloading " + repository.save(new Employee("Anıl Düz", ".NET Core Developer")));
        };
    }
}
