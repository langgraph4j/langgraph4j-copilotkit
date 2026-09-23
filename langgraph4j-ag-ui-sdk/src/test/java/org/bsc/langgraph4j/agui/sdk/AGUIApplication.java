package org.bsc.langgraph4j.agui.sdk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AGUIApplication {

    @Bean
    AGUIAgentRegistry createAgentExecutor() {

        return new AGUIAgentRegistry(
                new AGUIAgentExecutorINTERRUPT("INTERRUPT"),
                new AGUIAgentExecutorHITL("HITL"));
    }

    public static void main(String[] args) {
            SpringApplication.run(AGUIApplication.class, args);
        }

}
