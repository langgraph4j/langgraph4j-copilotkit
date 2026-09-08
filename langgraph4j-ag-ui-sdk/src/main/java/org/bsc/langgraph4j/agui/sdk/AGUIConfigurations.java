package org.bsc.langgraph4j.agui.sdk;

import com.agui.json.ObjectMapperFactory;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AGUIConfigurations {

    @Bean
    public ObjectMapper objectMapper() {
        JsonFactory factory = JsonFactory.builder()
                .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                .build();

        JsonMapper mapper = JsonMapper.builder(factory).build();

        ObjectMapperFactory.addMixins(mapper);

        return mapper;

    }

}
