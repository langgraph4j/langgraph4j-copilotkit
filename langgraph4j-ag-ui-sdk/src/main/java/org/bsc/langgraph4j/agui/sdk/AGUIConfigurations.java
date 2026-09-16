package org.bsc.langgraph4j.agui.sdk;

import com.agui.json.AGUIJacksonSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AGUIConfigurations {

    @Bean
    public com.agui.community.core.serialization.Serializer aguiSerializer() {

        JsonFactory factory = JsonFactory.builder()
                .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                .build();

        final var mapper = JsonMapper.builder(factory)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .defaultPropertyInclusion(JsonInclude.Value.ALL_NON_NULL)
                .build();

        return new AGUIJacksonSerializer(mapper);

    }

}
