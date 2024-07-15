package org.eyupkaan.restful;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository, OrderRepository orderRepository){
        return args -> {
            employeeRepository.save(new Employee("Eyüp Kaan", "Özteriş", "Java Developer"));
            employeeRepository.save(new Employee("Anıl", "Duz", ".NET Developer"));

            orderRepository.save(new Order("Macbook Pro", Status.COMPLETED));
            orderRepository.save(new Order("Asus Zenbook", Status.IN_PROGRESS));

            employeeRepository.findAll().forEach(e -> log.info("Preloaded " + e));
            orderRepository.findAll().forEach(o -> log.info("Preloaded " + o));
        };
    }
}