package org.bsc.langgraph4j.agui.sdk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

@Controller
public class AGUISSEController {

    private final AGUIAbstractLangGraphAgent agUiAgent;
    private final ObjectMapper objectMapper;

    public AGUISSEController(AGUIAbstractLangGraphAgent agUiAgent, ObjectMapper objectMapper) {
        this.agUiAgent = agUiAgent;
        this.objectMapper = objectMapper;
    }

    //@PostMapping(value = "/sse/{agentId}")
    public ResponseEntity<SseEmitter> streamDataWithSseEmitter(@PathVariable("agentId") final String agentId, @RequestBody() AGUIParameters parameters ) throws JsonProcessingException {
        final var emitter = new SseEmitter(Long.MAX_VALUE);

        this.agUiAgent.run(parameters.toRunAgentParameters())
                .subscribe(
                        ( event ) -> {
                            try {
                                final var sseEvent = SseEmitter.event()
                                            .data(" %s".formatted(objectMapper.writeValueAsString(event)))
                                            .build();
                                emitter.send(sseEvent);
                            } catch (Exception e) {
                                emitter.completeWithError(e);
                            }
                        },
                        emitter::completeWithError,
                        emitter::complete
                );

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noCache())
                .body(emitter);

    }

    @PostMapping(value = "/sse/{agentId}")
    public Flux<String> streamDataWithFlux(@PathVariable("agentId") final String agentId, @RequestBody() AGUIParameters parameters ) throws JsonProcessingException {

        return this.agUiAgent.run(parameters.toRunAgentParameters())
                .map( event -> {
                    try {
                        return " %s".formatted(objectMapper.writeValueAsString(event));
                    } catch (Exception e) {
                        throw new Error( e );
                    }
                });
    }

}
