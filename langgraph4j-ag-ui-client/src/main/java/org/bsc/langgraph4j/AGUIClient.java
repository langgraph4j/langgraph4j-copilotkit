package org.bsc.langgraph4j;

import com.agui.core.agent.RunAgentInput;
import com.agui.core.event.BaseEvent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class AGUIClient {

    private final java.net.http.HttpClient client;
    private final ObjectMapper objectMapper;
    private final URI uri;

    public AGUIClient(String url) {

        this.uri = URI.create(url);
        client = java.net.http.HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
        com.agui.json.ObjectMapperFactory.addMixins(this.objectMapper);

    }

    public <T> T getSync(String path, Map<String, ?> parameters, TypeReference<T> typeReference) throws Exception {
        requireNonNull( path, "path is required");
        requireNonNull( parameters, "parameters are required");
        requireNonNull( typeReference, "typeReference is required");

        final var request = java.net.http.HttpRequest.newBuilder()
                .uri(uriWithParameters(path, parameters))
                .header("Accept", "application/json")
                .GET()
                .build();

        final var response = client.send(
                request,
                java.net.http.HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IllegalStateException(
                    "GET request failed with status %d: %s".formatted(
                            response.statusCode(),
                            response.body()));
        }

        return this.objectMapper.readValue(response.body(), typeReference);
    }

    private URI uriWithParameters(String path, Map<String, ?> parameters) {
        if (parameters == null || parameters.isEmpty()) {
            return this.uri.resolve(path);
        }

        final var query = parameters.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .map(entry -> "%s=%s".formatted(
                        encodeQueryParam(entry.getKey()),
                        encodeQueryParam(String.valueOf(entry.getValue()))))
                .collect(Collectors.joining("&"));

        if (query.isBlank()) {
            return this.uri.resolve(path);
        }


        final var rawUri = this.uri.resolve(path).toASCIIString();
        final var fragmentIndex = rawUri.indexOf('#');
        final var uriWithoutFragment = fragmentIndex >= 0 ?
                rawUri.substring(0, fragmentIndex) :
                rawUri;
        final var fragment = fragmentIndex >= 0 ?
                rawUri.substring(fragmentIndex) :
                "";
        final var separator = uriWithoutFragment.contains("?")
                ? (uriWithoutFragment.endsWith("?") || uriWithoutFragment.endsWith("&") ? "" : "&")
                : "?";

        return  URI.create(uriWithoutFragment + separator + query + fragment);

    }

    private String encodeQueryParam(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8)
                .replace("+", "%20");
    }

    public void streamEventsSync(String path, RunAgentInput input, Consumer<BaseEvent> consumer) throws Exception {
        requireNonNull( path, "path is required");
        requireNonNull( input, "input is required");
        requireNonNull( consumer, "consumer is required");

        final var json = this.objectMapper.writeValueAsString(input);

        final var request = java.net.http.HttpRequest.newBuilder()
                .uri(this.uri.resolve(path))
                .header("Content-Type", "application/json")
                .header("Accept", "text/event-stream")
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(json))
                .build();

        final var response = client.send(
                request,
                java.net.http.HttpResponse.BodyHandlers.ofInputStream());

        //System.out.println("response: " + response);

        try (var reader = new BufferedReader(
                new InputStreamReader(response.body(), StandardCharsets.UTF_8))) {

            final var data = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {

                if (line.isBlank()) {
                    if (!data.isEmpty()) {

                        final var event = this.objectMapper.readValue(data.toString(), BaseEvent.class);
                        consumer.accept(event);

                        data.setLength(0);

                    }
                    continue;
                }

                if (line.startsWith("data:")) {
                    if (!data.isEmpty()) {
                        data.append('\n');
                    }
                    data.append(line.substring(5).trim());
                }
            }
        }
    }
}

