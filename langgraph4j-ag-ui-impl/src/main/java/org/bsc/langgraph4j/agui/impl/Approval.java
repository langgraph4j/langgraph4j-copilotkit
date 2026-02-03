package org.bsc.langgraph4j.agui.impl;


import static java.util.Objects.requireNonNull;

public record Approval(String toolId, String toolName, String toolArgs) {
    public Approval {
        requireNonNull( toolId, "toolId cannot ne bull");
        requireNonNull( toolName, "toolName cannot ne bull");
        requireNonNull( toolArgs, "toolArgs cannot ne bull");
    }
}
