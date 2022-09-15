package rest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import rest.service.model.DataResource;
import rest.service.repository.DataResourceRepository;

@SpringBootApplication
public class SpringBootWebApplication {

	private static final Logger LOG = LoggerFactory.getLogger(SpringBootWebApplication.class);
	
    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebApplication.class, args);
    }
    
    @Bean
    CommandLineRunner initDatabase(DataResourceRepository repository) {
        return args -> {
        	LOG.info("Preloading " + repository.save(new DataResource("data1", "data2", "data3")));
        };
    }
}
