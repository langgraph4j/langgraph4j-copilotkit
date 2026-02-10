package org.bsc.langgraph4j.agui.sdk;

import com.agui.core.message.Role;

import static java.util.Objects.requireNonNull;

public record ToolCallResultData(String toolCallId,
                                 String content,
                                 Role role) {
    public ToolCallResultData {
        requireNonNull(toolCallId, "toolCallId cannot be null");
        requireNonNull(content, "content cannot be null");
        requireNonNull(role, "role cannot be null");
    }

    public ToolCallResultData( String toolCallId, String content) {
        this( toolCallId, content, Role.tool);
    }
}
