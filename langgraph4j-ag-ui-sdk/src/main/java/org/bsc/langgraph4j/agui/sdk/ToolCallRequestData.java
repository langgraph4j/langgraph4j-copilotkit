package org.bsc.langgraph4j.agui.sdk;

import static java.util.Objects.requireNonNull;

public record ToolCallRequestData(String toolId, String toolName, String toolArgs) {
    public ToolCallRequestData {
        requireNonNull(toolId, "toolId cannot ne bull");
        requireNonNull(toolName, "toolName cannot ne bull");
        requireNonNull(toolArgs, "toolArgs cannot ne bull");
    }
}
