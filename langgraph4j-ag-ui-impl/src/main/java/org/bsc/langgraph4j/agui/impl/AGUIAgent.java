package org.bsc.langgraph4j.agui.impl;

import reactor.core.publisher.Flux;

public interface AGUIAgent {

    Flux<? extends AGUIEvent> run(AGUIType.RunAgentInput input );

}
