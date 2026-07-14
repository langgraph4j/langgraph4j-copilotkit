//DEPS org.bsc.langgraph4j:langgraph4j-ag-ui-sdk:1.9-SNAPSHOT
//DEPS org.bsc.langgraph4j:langgraph4j-javelit:1.9-SNAPSHOT
/// DEPS net.sourceforge.plantuml:plantuml-mit:1.2025.10
//DEPS org.springframework.ai:spring-ai-bom:2.0.0@pom
//DEPS org.springframework.ai:spring-ai-client-chat
//DEPS org.springframework.ai:spring-ai-openai
//DEPS org.springframework.ai:spring-ai-ollama
//DEPS org.springframework.ai:spring-ai-google-genai
///DEPS org.springframework.ai:spring-ai-azure-openai
//DEPS com.ag-ui.community:java-ok-http:4.12.0

import com.agui.core.agent.RunAgentInput;
import com.agui.core.event.BaseEvent;
import com.agui.core.message.Role;
import com.agui.core.state.State;
import com.agui.okhttp.HttpClient;
import io.javelit.core.Jt;
import org.bsc.javelit.JtSpinner;
import org.bsc.langgraph4j.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import com.agui.core.message.UserMessage;

public class JtAGUIClientApp {

    public static void main(String[] args) {

        var app = new JtAGUIClientApp();

        app.view();
    }


    public void view() {
        Jt.title("LangGraph4J AG UI Client").use();
        Jt.markdown("### Powered by LangGraph4j and SpringAI").use();

        final var threadId = UUID.randomUUID().toString();
        final var runId = UUID.randomUUID().toString();

        final var url = Jt.textInput("server url")
                .value("http://localhost:8081/sse/agent1")
                .use();

        var start = Jt.button("start agent")
                .disabled(url.isBlank())
                .use();

        if (start) {

            var spinner = JtSpinner.builder()
                    .message("**starting the agent** ....")
                    .use();

            try {
                final var startTime = Instant.now();

                final var client = new HttpClient(url);

                final var userMessage = new UserMessage();
                userMessage.setContent("Hello, I need help with my project.");
                userMessage.setName(Role.user.name());
                
                var input = new RunAgentInput(
                        threadId,
                        runId,
                        new State(),
                        List.of( userMessage ),
                        List.of(), // tools
                        List.of(), // context
                        "props" // forwardedProps
                );
                var cancellationToken = new AtomicBoolean(false);
                List<BaseEvent> receivedEvents = new ArrayList<>();

                var future = client.streamEvents(
                        input,
                        receivedEvents::add,
                        cancellationToken
                );
                future.get(1, TimeUnit.MINUTES);

                final var elapsedTime = Duration.between(startTime, Instant.now());

                final var events = receivedEvents.stream()
                        .map(Objects::toString)
                        .map("* %s"::formatted)
                        .collect(Collectors.joining("\n"));
                Jt.text(events).use();

                Jt.success("finished in %ds%n%n%s".formatted(elapsedTime.toSeconds(), "OK"))
                        .use(spinner);
            } catch (Exception e) {
                final var stackTrace = new StringWriter();
                e.printStackTrace(new PrintWriter(stackTrace));

                Jt.error("""
                        **Agent execution failed**

                        ```text
                        %s
                        ```
                        """.formatted(stackTrace.toString())).use(spinner);            }
        }

    }


}

