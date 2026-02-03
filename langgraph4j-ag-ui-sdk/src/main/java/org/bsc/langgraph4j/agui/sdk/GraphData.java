package org.bsc.langgraph4j.agui.sdk;

import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.state.AgentState;

import static java.util.Objects.requireNonNull;

public record GraphData(CompiledGraph<? extends AgentState> compiledGraph,
                        boolean interruption)
{
    public GraphData {
        requireNonNull( compiledGraph, "compiledGraph cannot ne bull");
    }

    public GraphData( CompiledGraph<? extends AgentState> compiledGraph) {
        this(compiledGraph, false);
    }

    public GraphData withInterruption( boolean interrupt ) {
        if( this.interruption == interrupt ) {
            return this;
        }
        return new GraphData(compiledGraph, interrupt);
    }
}
