package org.bsc.langgraph4j.ag_ui;

import reactor.core.publisher.Flux;

public interface AGUIAbstractAgent {

    Flux<? extends AGUIEvent> run(AGUIType.RunAgentInput input );

}
