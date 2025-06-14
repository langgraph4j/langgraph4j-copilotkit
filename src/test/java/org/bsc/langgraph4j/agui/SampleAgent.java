package org.bsc.langgraph4j.agui;

import reactor.core.publisher.Flux;

public class SampleAgent implements AGUIAbstractAgent {

    @Override
    public Flux<? extends AGUIEvent> run(AGUIType.RunAgentInput input) {
        return Flux.create( emitter -> {

            emitter.next( new AGUIEvent.RunStartedEvent(
                    input.threadId(),
                    input.runId()) );

            var messageId = String.valueOf(System.currentTimeMillis());

            emitter.next( new AGUIEvent.TextMessageStartEvent(messageId) );

            emitter.next( new AGUIEvent.TextMessageContentEvent(
                    messageId,
                    "Hello World!"));

            emitter.next( new AGUIEvent.TextMessageEndEvent(messageId) );

            emitter.next( new AGUIEvent.RunFinishedEvent(
                    input.threadId(),
                    input.runId()) );

        });
    }
}
