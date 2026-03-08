package org.bsc.langgraph4j.agui.sdk;

import com.agui.core.message.Role;

import static java.util.Objects.requireNonNull;

public record ToolCallData( Request request, Response response ) {

    public record Request(String toolId, String toolName, String toolArgs) {
        public Request {
            requireNonNull(toolId, "toolId cannot ne bull");
            requireNonNull(toolName, "toolName cannot ne bull");
            requireNonNull(toolArgs, "toolArgs cannot ne bull");
        }
    }
    public record Response(String toolCallId,
                                     String content,
                                     Role role) {
        public Response {
            requireNonNull(toolCallId, "toolCallId cannot be null");
            requireNonNull(content, "content cannot be null");
            requireNonNull(role, "role cannot be null");
        }

        public Response( String toolCallId, String content) {
            this( toolCallId, content, Role.tool);
        }
    }

}
