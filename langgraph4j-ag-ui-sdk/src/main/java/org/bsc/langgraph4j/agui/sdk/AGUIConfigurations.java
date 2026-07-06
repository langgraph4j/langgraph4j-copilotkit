package org.bsc.langgraph4j.agui.sdk;

import com.agui.core.message.*;
import com.agui.json.ObjectMapperFactory;
import com.agui.json.mixins.MessageMixin;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
