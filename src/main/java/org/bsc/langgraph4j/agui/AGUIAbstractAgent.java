package org.bsc.langgraph4j.agui;

import reactor.core.publisher.Flux;

public interface AGUIAbstractAgent {

    Flux<? extends AGUIEvent> run(AGUIType.RunAgentInput input );

}
