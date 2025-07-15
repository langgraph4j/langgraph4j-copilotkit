package org.bsc.langgraph4j.agui;

import com.fasterxml.jackson.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Base interface for all AG-UI messages, facilitating polymorphic deserialization.
 */
@JsonIgnoreProperties(ignoreUnknown = true) // Add this annotation
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY, // Use existing 'role' property for dispatch
        property = "type",
        visible = false // Make the 'role' property accessible after deserialization
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AGUIMessage.TextMessage.class, name = "TextMessage"),
        @JsonSubTypes.Type(value = AGUIMessage.ActionExecutionMessage.class, name = "ActionExecutionMessage"),
        @JsonSubTypes.Type(value = AGUIMessage.ResultMessage.class, name = "ResultMessage"),
})
@JsonInclude(JsonInclude.Include.NON_NULL) // Excludes null fields during serialization
public interface AGUIMessage {
    @JsonProperty("id")
    String id();

    @JsonProperty("createdAt")
    Date createdAt();

    interface HasRole {
        @JsonProperty("role")
        String role();

        default boolean isSystem() {
            return Objects.equals(role(), "system");
        }

        default boolean isUser() {
            return Objects.equals(role(), "user");
        }

        default boolean isAssistant() {
            return Objects.equals(role(), "assistant");
        }

    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    record TextMessage(
            @JsonProperty("id") String id,
            @JsonProperty("createdAt") Date createdAt,
            @JsonProperty("role") String role,
            @JsonProperty("content") String content,
            @JsonProperty("parentMessageId") String parentMessageId
    ) implements AGUIMessage, HasRole {

        public TextMessage {
            Objects.requireNonNull(id, "id cannot be null");
            Objects.requireNonNull(role, "role cannot be null");
            Objects.requireNonNull(content, "content cannot be null for system message");

        }
    }

    static TextMessage userMessage( String id, String content ) {
        return new TextMessage(id, new Date(), "user", content, null);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record ActionExecutionMessage(
            @JsonProperty("id") String id,
            @JsonProperty("createdAt") Date createdAt,
            @JsonProperty("name") String name,
            @JsonProperty("arguments") Map<String, Object> arguments,
            @JsonProperty("parentMessageId")String parentMessageId

    ) implements AGUIMessage {

        public ActionExecutionMessage {
            Objects.requireNonNull(id, "id cannot be null");
            Objects.requireNonNull(name, "name cannot be null");
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record ResultMessage(
            @JsonProperty("id") String id,
            @JsonProperty("createdAt") Date createdAt,
            @JsonProperty("actionExecutionId") String actionExecutionId,
            @JsonProperty("actionName") String actionName,
            @JsonProperty("result") String result

    ) implements AGUIMessage {

        public ResultMessage {
            Objects.requireNonNull(id, "id cannot be null");
            Objects.requireNonNull(actionExecutionId, "actionExecutionId cannot be null");
        }
    }

}
