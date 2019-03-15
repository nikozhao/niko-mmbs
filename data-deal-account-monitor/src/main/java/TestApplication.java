import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author: Niko Zhao
 * @Date: Create in 15:01 10/26/17
 */
@SpringBootApplication(exclude = {
        //DataSourceAutoConfiguration.class,
        //HibernateJpaAutoConfiguration.class,
        MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class
})
@ComponentScan("niko")
@EntityScan("niko.dao.entry")
@EnableJpaRepositories("niko.dao.repository")
@EnableAutoConfiguration
@EnableTransactionManagement
public class TestApplication {
    public static void main(String[] args) {
        // new SpringApplicationBuilder(TestApplication.class).run(args);
        SpringApplication.run(TestApplication.class, args);
    }
    /*@Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("Let's inspect the beans provided by Spring Boot:");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }
        };
    }*/
}
