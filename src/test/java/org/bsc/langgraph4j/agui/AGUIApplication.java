package org.bsc.langgraph4j.agui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AGUIApplication {

	@Bean
    AGUIAgent createAgent() {
		//return new AGUISampleAgent();
		return new AGUIAgentExecutor();
	}

	public static void main(String[] args) {
		SpringApplication.run(AGUIApplication.class, args);
	}

}
