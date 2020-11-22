package com.liamnbtech.server.client.blizzard.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public interface BlizzardApiRequest<R extends BlizzardApiResponse> {

    URI getUri(String region, ObjectMapper objectMapper) throws URISyntaxException, JsonProcessingException;

    HttpMethod getHttpMethod();

    Class<R> getResponseClass();
}
