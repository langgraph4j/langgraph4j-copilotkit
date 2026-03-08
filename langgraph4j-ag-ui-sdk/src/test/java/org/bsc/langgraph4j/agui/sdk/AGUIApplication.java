package org.bsc.langgraph4j.agui.sdk;

import com.agui.json.ObjectMapperFactory;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AGUIApplication {

    @Bean
    public ObjectMapper objectMapper() {
        JsonFactory factory = JsonFactory.builder()
        .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
        .build();
        var result = new ObjectMapper(factory);
        ObjectMapperFactory.addMixins(result);
        return result;
    }

    @Bean
    AGUIAbstractLangGraphAgent createAgentExecutor(ObjectMapper objectMapper) {

        return new AGUIAgentExecutor();
    }

    public static void main(String[] args) {
            SpringApplication.run(AGUIApplication.class, args);
        }

}
