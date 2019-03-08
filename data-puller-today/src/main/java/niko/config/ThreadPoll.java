package niko.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @Author: zhaozongqiang
 * @Date: Create in 2019/1/16
 */
@Configuration
public class ThreadPoll {

    @Bean("taskThreadPoll")
    public Executor threadPoll(){
        return Executors.newFixedThreadPool(6);
    }
}
