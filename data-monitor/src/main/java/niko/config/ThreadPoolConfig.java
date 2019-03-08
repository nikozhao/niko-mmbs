package niko.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: zhaozongqiang
 * @Date: Create in 2019/2/19
 */
@Configuration
public class ThreadPoolConfig {

    @Bean
    public ExecutorService threadPoolExecutor(){
        return Executors.newFixedThreadPool(3);
    }
}
