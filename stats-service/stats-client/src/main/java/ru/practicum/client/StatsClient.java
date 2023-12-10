package ru.practicum.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.EndPointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class StatsClient {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
    protected final RestTemplate rest;

    @Autowired
    public StatsClient(RestTemplateBuilder builder, @Value("${stats-server.url}") String serverUrl) {
        rest = builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl)).requestFactory(HttpComponentsClientHttpRequestFactory::new).build();
    }

    public ResponseEntity<Object> post(EndPointHitDto body) {
        String path = "/hit";
        return makeAndSendRequest(HttpMethod.POST, path, null, body);
    }

    public ResponseEntity<Object> get(LocalDateTime start, LocalDateTime end, Collection<String> uris, Boolean unique) {
        String formattedStart = start.format(formatter);
        String formattedEnd = end.format(formatter);
        String urisStr = String.join(",", uris);
        Map<String, Object> parameters = Map.of(
                "start", formattedStart,
                "end", formattedEnd,
                "uris", urisStr,
                "unique", unique);

        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }

    public ResponseEntity<Object> get(LocalDateTime start, LocalDateTime end, Collection<String> uris) {
        String formattedStart = start.format(formatter);
        String formattedEnd = end.format(formatter);
        String urisStr = String.join(",", uris);
        Map<String, Object> parameters = Map.of(
                "start", formattedStart,
                "end", formattedEnd,
                "uris", urisStr);

        return get("/stats?start={start}&end={end}&uris={uris}", parameters);
    }

    private ResponseEntity<Object> get(String path, Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, parameters, null);
    }

    private ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, @Nullable Map<String, Object> parameters, @Nullable EndPointHitDto body) {
        HttpEntity<EndPointHitDto> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<Object> statServerResponse;
        try {
            if (parameters != null) {
                statServerResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                statServerResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(statServerResponse);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }
}
