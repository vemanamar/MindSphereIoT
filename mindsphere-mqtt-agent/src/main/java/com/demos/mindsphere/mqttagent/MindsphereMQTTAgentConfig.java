package com.demos.mindsphere.mqttagent;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Configuration for MQTT Agent
 * 
 * @author Amar Vemanaboyina
 * @version 1.0
 * 
 *
 */
@Configuration
@EnableAsync(proxyTargetClass = true)
public class MindsphereMQTTAgentConfig {
	
	@Bean
    public TaskExecutor threadPoolTaskExecutor() {
 
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(100);       
        executor.initialize(); 
        return executor;
    }
	

}
