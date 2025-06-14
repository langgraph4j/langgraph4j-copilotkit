package org.bsc.langgraph4j.ag_ui;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;
import java.util.Objects;

/**
 * Base interface for all AG-UI messages, facilitating polymorphic deserialization.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY, // Use existing 'role' property for dispatch
        property = "role",
        visible = true // Make the 'role' property accessible after deserialization
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AGUIMessage.DeveloperMessage.class, name = AGUIMessage.DeveloperMessage.ROLE),
        @JsonSubTypes.Type(value = AGUIMessage.SystemMessage.class, name = AGUIMessage.SystemMessage.ROLE),
        @JsonSubTypes.Type(value = AGUIMessage.AssistantMessage.class, name = AGUIMessage.AssistantMessage.ROLE),
        @JsonSubTypes.Type(value = AGUIMessage.UserMessage.class, name = AGUIMessage.UserMessage.ROLE),
        @JsonSubTypes.Type(value = AGUIMessage.ToolMessage.class, name = AGUIMessage.ToolMessage.ROLE)
})
@JsonInclude(JsonInclude.Include.NON_NULL) // Excludes null fields during serialization
public interface AGUIMessage {
    @JsonProperty("id")
    String id();

    @JsonProperty("role")
    String role();

    @JsonProperty("content")
    String content(); // Optionality handled by subtypes or nullability

    @JsonProperty("name")
    String name();     // Optional, can be null


    /**
     * Message from a developer.
     * Role: "developer"
     * Content: string (required)
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    record DeveloperMessage(
            @JsonProperty("id") String id,
            // role is implicitly "developer" due to @JsonSubTypes
            @JsonProperty("content") String content,
            @JsonProperty("name") String name
    ) implements AGUIMessage {
        public static final String ROLE = "developer";

        public DeveloperMessage {
            Objects.requireNonNull(id, "id cannot be null");
            Objects.requireNonNull(content, "content cannot be null for developer message");
        }

        @Override
        public String role() {
            return ROLE;
        }

        // Convenience constructor
        public DeveloperMessage(String id, String content) {
            this(id, content, null);
        }
    }

    /**
     * System message.
     * Role: "system"
     * Content: string (required)
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record SystemMessage(
            @JsonProperty("id") String id,
            @JsonProperty("content") String content,
            @JsonProperty("name") String name
    ) implements AGUIMessage {
        public static final String ROLE = "system";

        public SystemMessage {
            Objects.requireNonNull(id, "id cannot be null");
            Objects.requireNonNull(content, "content cannot be null for system message");
        }

        @Override
        public String role() {
            return ROLE;
        }

        // Convenience constructor
        public SystemMessage(String id, String content) {
            this(id, content, null);
        }
    }

    /**
     * Assistant message.
     * Role: "assistant"
     * Content: string (optional)
     * ToolCalls: ToolCall[] (optional)
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    record AssistantMessage(
            @JsonProperty("id") String id,
            @JsonProperty("content") String content, // Optional
            @JsonProperty("name") String name,       // Optional
            @JsonProperty("tool_calls") List<AGUIType.ToolCall> toolCalls // Optional
    ) implements AGUIMessage {
        public static final String ROLE = "assistant";

        public AssistantMessage {
            Objects.requireNonNull(id, "id cannot be null");
        }

        @Override
        public String role() {
            return ROLE;
        }

        // Convenience constructor
        public AssistantMessage(String id, String content, List<AGUIType.ToolCall> toolCalls) {
            this(id, content, null, toolCalls);
        }

        public AssistantMessage(String id, String content) {
            this(id, content, null, null);
        }
    }

    /**
     * User message.
     * Role: "user"
     * Content: string (required)
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    record UserMessage(
            @JsonProperty("id") String id,
            @JsonProperty("content") String content,
            @JsonProperty("name") String name
    ) implements AGUIMessage {
        public static final String ROLE = "user";

        public UserMessage {
            Objects.requireNonNull(id, "id cannot be null");
            Objects.requireNonNull(content, "content cannot be null for user message");
        }

        @Override
        public String role() {
            return ROLE;
        }

        // Convenience constructor
        public UserMessage(String id, String content) {
            this(id, content, null);
        }
    }

    /**
     * Tool message (response from a tool).
     * Role: "tool"
     * Content: string (required)
     * ToolCallId: string (required)
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    record ToolMessage(
            @JsonProperty("id") String id,
            @JsonProperty("content") String content,
            @JsonProperty("tool_call_id") String toolCallId
            // name is not typically part of a tool message response based on common patterns
    ) implements AGUIMessage {
        public static final String ROLE = "tool";

        public ToolMessage {
            Objects.requireNonNull(id, "id cannot be null");
            Objects.requireNonNull(content, "content cannot be null for tool message");
            Objects.requireNonNull(toolCallId, "toolCallId cannot be null");
        }

        @Override
        public String role() {
            return ROLE;
        }

        @Override
        public String name() {
            return null;
        } // Explicitly null as 'name' isn't standard here

    }
}
