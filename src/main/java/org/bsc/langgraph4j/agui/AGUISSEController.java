package org.bsc.langgraph4j.agui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalTime;

@RestController
@RequestMapping("/langgraph4j")
public class AGUISSEController {

    final AGUIAbstractAgent uiAgent;
    final ObjectMapper mapper = new ObjectMapper();

    public AGUISSEController(AGUIAbstractAgent uiAgent) {
        this.uiAgent = uiAgent;
    }

    @PostMapping(path = "/copilotkit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<AGUIEvent>> copilotKit(@RequestBody String runAgentInputPayload) throws Exception {

        var input = mapper.readValue(runAgentInputPayload, AGUIType.RunAgentInput.class);
        return uiAgent.run( input )
                .map( event -> ServerSentEvent.<AGUIEvent>builder()
                        .id(input.threadId())
                        .data(event)
                .build());
    }
    /**
     * Endpoint to stream Server-Sent Events.
     * This example emits a message every second with the current time.
     *
     * @return A Flux of ServerSentEvent objects.
     */
    @GetMapping(path = "/sse-events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamEvents() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> ServerSentEvent.<String>builder()
                        .id(String.valueOf(sequence))
                        .event("periodic-event")
                        .data("SSE - " + LocalTime.now().toString())
                        .comment("This is a comment for event " + sequence)
                        .retry(Duration.ofSeconds(5)) // Client should retry after 5 seconds if connection is lost
                        .build());
    }

    /**
     * A more complex example demonstrating different event types and data structures.
     *
     * @return A Flux of ServerSentEvent objects.
     */
    @GetMapping(path = "/sse-complex-events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Object>> streamComplexEvents() {
        // Simulate different types of events
        Flux<ServerSentEvent<Object>> heartBeat = Flux.interval(Duration.ofSeconds(10))
                .map(seq -> ServerSentEvent.builder()
                        .comment("keep-alive")
                        .build());

        Flux<ServerSentEvent<Object>> dataEvents = Flux.interval(Duration.ofSeconds(2))
                .take(5) // Emit 5 data events
                .map(sequence -> {
                    // Simulate different event names and data
                    if (sequence % 2 == 0) {
                        return ServerSentEvent.builder()
                                .id("data-" + sequence)
                                .event("data-update")
                                .data(new DataObject("Item " + sequence, (int) (sequence * 100)))
                                .build();
                    } else {
                        return ServerSentEvent.builder()
                                .id("alert-" + sequence)
                                .event("alert")
                                .data("Critical Alert at " + LocalTime.now())
                                .build();
                    }
                });

        Flux<ServerSentEvent<Object>> completionEvent = Flux.just(
                ServerSentEvent.builder()
                        .event("stream-completed")
                        .data("The event stream has finished.")
                        .build()
        ).delayElements(Duration.ofSeconds(11)); // Ensure it's sent after data events

        return Flux.merge(heartBeat, dataEvents, completionEvent);
    }

    // Example data object for complex events
    private record DataObject(String name, int value) {}

}