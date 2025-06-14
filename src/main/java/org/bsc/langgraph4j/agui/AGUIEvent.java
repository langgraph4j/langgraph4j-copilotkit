package org.bsc.langgraph4j.agui;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;
import java.util.Objects;

/**
 * Base interface for all events in the Agent User Interaction Protocol.
 * Uses Jackson annotations for polymorphic deserialization based on the 'type' field.
 * This interface effectively replaces the content of the provided AGUIEvent.java.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME, // Use enum names for type
        include = JsonTypeInfo.As.EXISTING_PROPERTY, // Use existing 'type' property for dispatch
        property = "type", // The property in JSON that holds the type identifier
        visible = true // Make the 'type' property accessible after deserialization
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AGUIEvent.TextMessageStartEvent.class, name = "TEXT_MESSAGE_START"),
        @JsonSubTypes.Type(value = AGUIEvent.TextMessageContentEvent.class, name = "TEXT_MESSAGE_CONTENT"),
        @JsonSubTypes.Type(value = AGUIEvent.TextMessageEndEvent.class, name = "TEXT_MESSAGE_END"),
        @JsonSubTypes.Type(value = AGUIEvent.TextMessageChunkEvent.class, name = "TEXT_MESSAGE_CHUNK"),
        @JsonSubTypes.Type(value = AGUIEvent.ToolCallStartEvent.class, name = "TOOL_CALL_START"),
        @JsonSubTypes.Type(value = AGUIEvent.ToolCallArgsEvent.class, name = "TOOL_CALL_ARGS"),
        @JsonSubTypes.Type(value = AGUIEvent.ToolCallEndEvent.class, name = "TOOL_CALL_END"),
        @JsonSubTypes.Type(value = AGUIEvent.ToolCallChunkEvent.class, name = "TOOL_CALL_CHUNK"),
        @JsonSubTypes.Type(value = AGUIEvent.StateSnapshotEvent.class, name = "STATE_SNAPSHOT"),
        @JsonSubTypes.Type(value = AGUIEvent.StateDeltaEvent.class, name = "STATE_DELTA"),
        @JsonSubTypes.Type(value = AGUIEvent.MessagesSnapshotEvent.class, name = "MESSAGES_SNAPSHOT"),
        @JsonSubTypes.Type(value = AGUIEvent.RawEvent.class, name = "RAW"),
        @JsonSubTypes.Type(value = AGUIEvent.CustomEvent.class, name = "CUSTOM"),
        @JsonSubTypes.Type(value = AGUIEvent.RunStartedEvent.class, name = "RUN_STARTED"),
        @JsonSubTypes.Type(value = AGUIEvent.RunFinishedEvent.class, name = "RUN_FINISHED"),
        @JsonSubTypes.Type(value = AGUIEvent.RunErrorEvent.class, name = "RUN_ERROR"),
        @JsonSubTypes.Type(value = AGUIEvent.StepStartedEvent.class, name = "STEP_STARTED"),
        @JsonSubTypes.Type(value = AGUIEvent.StepFinishedEvent.class, name = "STEP_FINISHED")
})
@JsonInclude(JsonInclude.Include.NON_NULL) // Excludes null fields during serialization
public interface AGUIEvent {
    @JsonProperty("type")
    EventType type();

    @JsonProperty("timestamp")
    Long timestamp(); // Optional, maps to Python's Optional[int]

    @JsonProperty("raw_event")
    Object rawEvent(); // Optional, maps to Python's Optional[Any]

    /**
     * Defines the event types for the Agent User Interaction Protocol.
     */
    enum EventType {
        TEXT_MESSAGE_START,
        TEXT_MESSAGE_CONTENT,
        TEXT_MESSAGE_END,
        TEXT_MESSAGE_CHUNK,
        TOOL_CALL_START,
        TOOL_CALL_ARGS,
        TOOL_CALL_END,
        TOOL_CALL_CHUNK,
        STATE_SNAPSHOT,
        STATE_DELTA,
        MESSAGES_SNAPSHOT,
        RAW,
        CUSTOM,
        RUN_STARTED,
        RUN_FINISHED,
        RUN_ERROR,
        STEP_STARTED,
        STEP_FINISHED
    }

    String ASSISTANT_ROLE = "assistant";

    // Placeholder for State and Message types.
    // Replace with actual AGUITypes.State and AGUITypes.Message if available.
    record State(@JsonProperty("value") Object value) {}
    record Message(@JsonProperty("value") Object value) {}


    @JsonInclude(JsonInclude.Include.NON_NULL)
    record TextMessageStartEvent(
            @JsonProperty("type") EventType type,
            @JsonProperty("timestamp") Long timestamp,
            @JsonProperty("raw_event") Object rawEvent,
            @JsonProperty("message_id") String messageId,
            @JsonProperty("role") String role
    ) implements AGUIEvent {
        public TextMessageStartEvent {
            Objects.requireNonNull(messageId, "messageId cannot be null");
            Objects.requireNonNull(role, "role cannot be null");
            if (type != EventType.TEXT_MESSAGE_START) {
                throw new IllegalArgumentException("Type must be TEXT_MESSAGE_START");
            }
            if (!ASSISTANT_ROLE.equals(role)) {
                throw new IllegalArgumentException("Role must be 'assistant'");
            }
        }
        public TextMessageStartEvent(String messageId) {
            this(EventType.TEXT_MESSAGE_START, System.currentTimeMillis(), null, messageId, ASSISTANT_ROLE);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record TextMessageContentEvent(
            @JsonProperty("type") EventType type,
            @JsonProperty("timestamp") Long timestamp,
            @JsonProperty("raw_event") Object rawEvent,
            @JsonProperty("message_id") String messageId,
            @JsonProperty("delta") String delta
    ) implements AGUIEvent {
        public TextMessageContentEvent {
            Objects.requireNonNull(messageId, "messageId cannot be null");
            Objects.requireNonNull(delta, "delta cannot be null");
            if (delta.isEmpty()) {
                throw new IllegalArgumentException("Delta must not be an empty string");
            }
            if (type != EventType.TEXT_MESSAGE_CONTENT) {
                throw new IllegalArgumentException("Type must be TEXT_MESSAGE_CONTENT");
            }
        }
        public TextMessageContentEvent(String messageId, String delta) {
            this(EventType.TEXT_MESSAGE_CONTENT, System.currentTimeMillis(), null, messageId, delta);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record TextMessageEndEvent(
            @JsonProperty("type") EventType type,
            @JsonProperty("timestamp") Long timestamp,
            @JsonProperty("raw_event") Object rawEvent,
            @JsonProperty("message_id") String messageId
    ) implements AGUIEvent {
        public TextMessageEndEvent {
            Objects.requireNonNull(messageId, "messageId cannot be null");
            if (type != EventType.TEXT_MESSAGE_END) {
                throw new IllegalArgumentException("Type must be TEXT_MESSAGE_END");
            }
        }
        public TextMessageEndEvent(String messageId) {
            this(EventType.TEXT_MESSAGE_END, System.currentTimeMillis(), null, messageId);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record TextMessageChunkEvent(
            @JsonProperty("type") EventType type,
            @JsonProperty("timestamp") Long timestamp,
            @JsonProperty("raw_event") Object rawEvent,
            @JsonProperty("message_id") String messageId, // Optional
            @JsonProperty("role") String role,           // Optional
            @JsonProperty("delta") String delta          // Optional
    ) implements AGUIEvent {
        public TextMessageChunkEvent {
            if (type != EventType.TEXT_MESSAGE_CHUNK) {
                throw new IllegalArgumentException("Type must be TEXT_MESSAGE_CHUNK");
            }
            if (role != null && !ASSISTANT_ROLE.equals(role)) {
                throw new IllegalArgumentException("Role, if present, must be 'assistant'");
            }
        }
        public TextMessageChunkEvent(String messageId, String role, String delta) {
            this(EventType.TEXT_MESSAGE_CHUNK, System.currentTimeMillis(), null, messageId, role, delta);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record ToolCallStartEvent(
            @JsonProperty("type") EventType type,
            @JsonProperty("timestamp") Long timestamp,
            @JsonProperty("raw_event") Object rawEvent,
            @JsonProperty("tool_call_id") String toolCallId,
            @JsonProperty("tool_call_name") String toolCallName,
            @JsonProperty("parent_message_id") String parentMessageId // Optional
    ) implements AGUIEvent {
        public ToolCallStartEvent {
            Objects.requireNonNull(toolCallId, "toolCallId cannot be null");
            Objects.requireNonNull(toolCallName, "toolCallName cannot be null");
            if (type != EventType.TOOL_CALL_START) {
                throw new IllegalArgumentException("Type must be TOOL_CALL_START");
            }
        }
        public ToolCallStartEvent(String toolCallId, String toolCallName, String parentMessageId) {
            this(EventType.TOOL_CALL_START, System.currentTimeMillis(), null, toolCallId, toolCallName, parentMessageId);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record ToolCallArgsEvent(
            @JsonProperty("type") EventType type,
            @JsonProperty("timestamp") Long timestamp,
            @JsonProperty("raw_event") Object rawEvent,
            @JsonProperty("tool_call_id") String toolCallId,
            @JsonProperty("delta") String delta
    ) implements AGUIEvent {
        public ToolCallArgsEvent {
            Objects.requireNonNull(toolCallId, "toolCallId cannot be null");
            Objects.requireNonNull(delta, "delta cannot be null");
            if (type != EventType.TOOL_CALL_ARGS) {
                throw new IllegalArgumentException("Type must be TOOL_CALL_ARGS");
            }
        }
        public ToolCallArgsEvent(String toolCallId, String delta) {
            this(EventType.TOOL_CALL_ARGS, System.currentTimeMillis(), null, toolCallId, delta);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record ToolCallEndEvent(
            @JsonProperty("type") EventType type,
            @JsonProperty("timestamp") Long timestamp,
            @JsonProperty("raw_event") Object rawEvent,
            @JsonProperty("tool_call_id") String toolCallId
    ) implements AGUIEvent {
        public ToolCallEndEvent {
            Objects.requireNonNull(toolCallId, "toolCallId cannot be null");
            if (type != EventType.TOOL_CALL_END) {
                throw new IllegalArgumentException("Type must be TOOL_CALL_END");
            }
        }
        public ToolCallEndEvent(String toolCallId) {
            this(EventType.TOOL_CALL_END, System.currentTimeMillis(), null, toolCallId);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record ToolCallChunkEvent(
            @JsonProperty("type") EventType type,
            @JsonProperty("timestamp") Long timestamp,
            @JsonProperty("raw_event") Object rawEvent,
            @JsonProperty("tool_call_id") String toolCallId,         // Optional
            @JsonProperty("tool_call_name") String toolCallName,     // Optional
            @JsonProperty("parent_message_id") String parentMessageId, // Optional
            @JsonProperty("delta") String delta                      // Optional
    ) implements AGUIEvent {
        public ToolCallChunkEvent {
            if (type != EventType.TOOL_CALL_CHUNK) {
                throw new IllegalArgumentException("Type must be TOOL_CALL_CHUNK");
            }
        }
        public ToolCallChunkEvent(String toolCallId, String toolCallName, String parentMessageId, String delta) {
            this(EventType.TOOL_CALL_CHUNK, System.currentTimeMillis(), null, toolCallId, toolCallName, parentMessageId, delta);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record StateSnapshotEvent(
            @JsonProperty("type") EventType type,
            @JsonProperty("timestamp") Long timestamp,
            @JsonProperty("raw_event") Object rawEvent,
            @JsonProperty("snapshot") State snapshot
    ) implements AGUIEvent {
        public StateSnapshotEvent {
            Objects.requireNonNull(snapshot, "snapshot cannot be null");
            if (type != EventType.STATE_SNAPSHOT) {
                throw new IllegalArgumentException("Type must be STATE_SNAPSHOT");
            }
        }
        public StateSnapshotEvent(State snapshot) {
            this(EventType.STATE_SNAPSHOT, System.currentTimeMillis(), null, snapshot);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record StateDeltaEvent(
            @JsonProperty("type") EventType type,
            @JsonProperty("timestamp") Long timestamp,
            @JsonProperty("raw_event") Object rawEvent,
            @JsonProperty("delta") List<Object> delta // Represents JSON Patch operations
    ) implements AGUIEvent {
        public StateDeltaEvent {
            Objects.requireNonNull(delta, "delta cannot be null");
            if (type != EventType.STATE_DELTA) {
                throw new IllegalArgumentException("Type must be STATE_DELTA");
            }
        }
        public StateDeltaEvent(List<Object> delta) {
            this(EventType.STATE_DELTA, System.currentTimeMillis(), null, delta);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record MessagesSnapshotEvent(
            @JsonProperty("type") EventType type,
            @JsonProperty("timestamp") Long timestamp,
            @JsonProperty("raw_event") Object rawEvent,
            @JsonProperty("messages") List<Message> messages
    ) implements AGUIEvent {
        public MessagesSnapshotEvent {
            Objects.requireNonNull(messages, "messages cannot be null");
            if (type != EventType.MESSAGES_SNAPSHOT) {
                throw new IllegalArgumentException("Type must be MESSAGES_SNAPSHOT");
            }
        }
        public MessagesSnapshotEvent(List<Message> messages) {
            this(EventType.MESSAGES_SNAPSHOT, System.currentTimeMillis(), null, messages);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record RawEvent(
            @JsonProperty("type") EventType type,
            @JsonProperty("timestamp") Long timestamp,
            @JsonProperty("raw_event") Object rawEvent, // This is the base raw_event
            @JsonProperty("event") Object event,       // This is the specific 'event' field for RawEvent
            @JsonProperty("source") String source      // Optional
    ) implements AGUIEvent {
        public RawEvent {
            Objects.requireNonNull(event, "event cannot be null for RawEvent");
            if (type != EventType.RAW) {
                throw new IllegalArgumentException("Type must be RAW");
            }
        }
        // Constructor for when base raw_event is not the same as this event's data
        public RawEvent(Object baseRawEventData, Object specificEventData, String source) {
            this(EventType.RAW, System.currentTimeMillis(), baseRawEventData, specificEventData, source);
        }
        // Constructor when this event's data is also the base raw_event
        public RawEvent(Object eventData, String source) {
            this(EventType.RAW, System.currentTimeMillis(), eventData, eventData, source);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record CustomEvent(
            @JsonProperty("type") EventType type,
            @JsonProperty("timestamp") Long timestamp,
            @JsonProperty("raw_event") Object rawEvent,
            @JsonProperty("name") String name,
            @JsonProperty("value") Object value
    ) implements AGUIEvent {
        public CustomEvent {
            Objects.requireNonNull(name, "name cannot be null");
            // Value can be any type, including null, so Objects.requireNonNull(value) might be too strict
            // depending on whether 'Any' in Python implies non-null. Assuming it can be null.
            if (type != EventType.CUSTOM) {
                throw new IllegalArgumentException("Type must be CUSTOM");
            }
        }
        public CustomEvent(String name, Object value) {
            this(EventType.CUSTOM, System.currentTimeMillis(), null, name, value);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record RunStartedEvent(
            @JsonProperty("type") EventType type,
            @JsonProperty("timestamp") Long timestamp,
            @JsonProperty("raw_event") Object rawEvent,
            @JsonProperty("thread_id") String threadId,
            @JsonProperty("run_id") String runId
    ) implements AGUIEvent {
        public RunStartedEvent {
            Objects.requireNonNull(threadId, "threadId cannot be null");
            // Objects.requireNonNull(runId, "runId cannot be null");
            if (type != EventType.RUN_STARTED) {
                throw new IllegalArgumentException("Type must be RUN_STARTED");
            }
        }
        public RunStartedEvent(String threadId, String runId) {
            this(EventType.RUN_STARTED, System.currentTimeMillis(), null, threadId, runId);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record RunFinishedEvent(
            @JsonProperty("type") EventType type,
            @JsonProperty("timestamp") Long timestamp,
            @JsonProperty("raw_event") Object rawEvent,
            @JsonProperty("thread_id") String threadId,
            @JsonProperty("run_id") String runId
    ) implements AGUIEvent {
        public RunFinishedEvent {
            Objects.requireNonNull(threadId, "threadId cannot be null");
            // Objects.requireNonNull(runId, "runId cannot be null");
            if (type != EventType.RUN_FINISHED) {
                throw new IllegalArgumentException("Type must be RUN_FINISHED");
            }
        }
        public RunFinishedEvent(String threadId, String runId) {
            this(EventType.RUN_FINISHED, System.currentTimeMillis(), null, threadId, runId);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record RunErrorEvent(
            @JsonProperty("type") EventType type,
            @JsonProperty("timestamp") Long timestamp,
            @JsonProperty("raw_event") Object rawEvent,
            @JsonProperty("message") String message,
            @JsonProperty("code") String code // Optional
    ) implements AGUIEvent {
        public RunErrorEvent {
            Objects.requireNonNull(message, "message cannot be null");
            if (type != EventType.RUN_ERROR) {
                throw new IllegalArgumentException("Type must be RUN_ERROR");
            }
        }
        public RunErrorEvent(String message, String code) {
            this(EventType.RUN_ERROR, System.currentTimeMillis(), null, message, code);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record StepStartedEvent(
            @JsonProperty("type") EventType type,
            @JsonProperty("timestamp") Long timestamp,
            @JsonProperty("raw_event") Object rawEvent,
            @JsonProperty("step_name") String stepName
    ) implements AGUIEvent {
        public StepStartedEvent {
            Objects.requireNonNull(stepName, "stepName cannot be null");
            if (type != EventType.STEP_STARTED) {
                throw new IllegalArgumentException("Type must be STEP_STARTED");
            }
        }
        public StepStartedEvent(String stepName) {
            this(EventType.STEP_STARTED, System.currentTimeMillis(), null, stepName);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record StepFinishedEvent(
            @JsonProperty("type") EventType type,
            @JsonProperty("timestamp") Long timestamp,
            @JsonProperty("raw_event") Object rawEvent,
            @JsonProperty("step_name") String stepName
    ) implements AGUIEvent {
        public StepFinishedEvent {
            Objects.requireNonNull(stepName, "stepName cannot be null");
            if (type != EventType.STEP_FINISHED) {
                throw new IllegalArgumentException("Type must be STEP_FINISHED");
            }
        }
        public StepFinishedEvent(String stepName) {
            this(EventType.STEP_FINISHED, System.currentTimeMillis(), null, stepName);
        }
    }
}

