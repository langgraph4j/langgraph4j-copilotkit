package com.agui.json;

import com.agui.community.core.event.Event;
import com.agui.community.core.interrupt.InterruptOutcome;
import com.agui.community.core.message.Message;
import com.agui.community.core.serialization.Serializer;
import com.agui.json.mixins.EventMixin;
import com.agui.json.mixins.InterruptMixin;
import com.agui.json.mixins.MessageMixin;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;

public class AGUIJacksonSerializer implements Serializer {

    private final ObjectMapper objectMapper;

    public AGUIJacksonSerializer(ObjectMapper objectMapper) {
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper must not be null");

        // ADD MIXINS FOR AGUI CORE CLASSES IF NOT ALREADY PRESENT
        if (Objects.isNull(objectMapper.findMixInClassFor(Message.class))) {
            objectMapper.addMixIn(Message.class, MessageMixin.class);
        }
        if (Objects.isNull(objectMapper.findMixInClassFor(Event.class))) {
            objectMapper.addMixIn(Event.class, EventMixin.class);
        }
        if (Objects.isNull(objectMapper.findMixInClassFor(InterruptOutcome.class))) {
            objectMapper.addMixIn(InterruptOutcome.class, InterruptMixin.class);
        }
    }

    public AGUIJacksonSerializer() {
        this(new ObjectMapper());
    }

    @Override
    public String serialize(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }

    @Override
    public <T> T deserialize(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize JSON to object", e);
        }
    }

    @Override
    public <T> List<T> deserializeList(String json, Class<T> elementType) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize JSON to list", e);
        }
    }
}
