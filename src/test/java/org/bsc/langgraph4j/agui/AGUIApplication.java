package org.bsc.langgraph4j.agui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AGUIApplication {

	@Bean("AGUIAgent")
	@ConditionalOnProperty(name = "ag-ui.agent", havingValue = "agentExecutor")
	AGUIAgent createAgentExecutor() {
		return new AGUIAgentExecutor();
	}

	@Bean("AGUIAgent")
	@ConditionalOnProperty(name = "ag-ui.agent", havingValue = "sample")
	AGUIAgent createSampleAgent() {
		return new AGUISampleAgent();
	}

	public static void main(String[] args) {
		SpringApplication.run(AGUIApplication.class, args);
	}

}
