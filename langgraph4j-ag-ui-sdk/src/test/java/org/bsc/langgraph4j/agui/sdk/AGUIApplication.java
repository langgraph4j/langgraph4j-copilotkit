package org.bsc.langgraph4j.agui.sdk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AGUIApplication {

    @Bean
    AGUIAbstractLangGraphAgent createAgentExecutor() {

        return new AGUIAgentExecutor();
    }

    public static void main(String[] args) {
            SpringApplication.run(AGUIApplication.class, args);
        }

}
