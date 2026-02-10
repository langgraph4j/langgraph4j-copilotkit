package org.bsc.langgraph4j.agui.sdk;

import com.agui.json.ObjectMapperFactory;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SpringBootApplication
public class AGUIApplication {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public interface ToolParametersMixin {
    }

    @Bean
    public ObjectMapper objectMapper() {
        JsonFactory factory = JsonFactory.builder()
        .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
        .build();
        var result = new ObjectMapper(factory);
        result.addMixIn(com.agui.core.tool.Tool.ToolParameters.class, ToolParametersMixin.class);
        ObjectMapperFactory.addMixins(result);
        return result;
    }


    public static void main(String[] args) {
            SpringApplication.run(AGUIApplication.class, args);
        }

}
