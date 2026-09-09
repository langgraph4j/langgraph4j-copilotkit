package org.bsc.langgraph4j.agui.sdk;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

@Controller
public class AGUISSEController {

    private final AGUIAbstractLangGraphAgent aguiAgent;
    private final com.agui.community.core.serialization.Serializer aguiSerializer;

    public AGUISSEController(AGUIAbstractLangGraphAgent aguiAgent, com.agui.community.core.serialization.Serializer  aguiSerializer) {
        this.aguiAgent = aguiAgent;
        this.aguiSerializer = aguiSerializer;
    }

    @PostMapping(
            value = "/sse/{agentId}",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> streamDataWithSseEmitter(
            @PathVariable("agentId") String agentId,
            @RequestBody String params
    ) throws JsonProcessingException {

        final var emitter = new SseEmitter(0L);
        final var parameters = aguiSerializer.deserialize(params, AGUIRunAgentInput.class);

        final var disposable = this.aguiAgent.run(parameters.toRunAgentParameters())
                .subscribe(
                        event -> {
                            try {
                                emitter.send(
                                        SseEmitter.event()
                                                .name("message")
                                                .data(aguiSerializer.serialize(event), MediaType.APPLICATION_JSON)
                                );
                            } catch (Exception e) {
                                emitter.completeWithError(e);
                            }
                        },
                        emitter::completeWithError,
                        emitter::complete
                );

        emitter.onCompletion(disposable::dispose);
        emitter.onTimeout(disposable::dispose);
        emitter.onError(error -> disposable.dispose());


        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .cacheControl(CacheControl.noCache())
                .header("X-Accel-Buffering", "no")
                .body(emitter);
    }

    //@PostMapping(value = "/sse/{agentId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PostMapping(value = "/flux/{agentId}")
    public Flux<String> streamDataWithFlux(@PathVariable("agentId") final String agentId, @RequestBody() String  params ) throws JsonProcessingException {

        final var parameters = aguiSerializer.deserialize(params, AGUIRunAgentInput.class);

        return this.aguiAgent.run(parameters.toRunAgentParameters())
                .map( event -> {
                    try {
                        final var json = aguiSerializer.serialize(event);
                        return " %s".formatted(json);
                    } catch (Exception e) {
                        throw new Error( e );
                    }
                });
    }

}
