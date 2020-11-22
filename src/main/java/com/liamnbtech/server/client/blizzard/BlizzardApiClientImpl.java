package com.liamnbtech.server.client.blizzard;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liamnbtech.server.client.blizzard.entity.BlizzardApiRequest;
import com.liamnbtech.server.client.blizzard.entity.BlizzardApiResponse;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlizzardApiClientImpl implements BlizzardApiClient {

    private final static String OAUTH_TOKEN_HTTP_HEADER_NAME = "Authorization";
    private final static String OAUTH_TOKEN_HTTP_HEADER_VALUE_PATTERN = "Bearer %s";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final BlizzardApiRegion region;
    private final String clientId;
    private final String clientSecret;

    private final AtomicReference<ClientState> clientState;
    private final Lock initializationLock;
    private final AtomicReference<String> clientAccessToken;

    public BlizzardApiClientImpl(
            HttpClient httpClient,
            ObjectMapper objectMapper,
            BlizzardApiRegion region,
            String clientId,
            String clientSecret) {

        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.region = region;
        this.clientId = clientId;
        this.clientSecret = clientSecret;

        this.clientState = new AtomicReference<>(ClientState.UNINITIALIZED);
        this.clientAccessToken = new AtomicReference<>();
        this.initializationLock = new ReentrantLock();
    }

    @Override
    public <R extends BlizzardApiResponse> CompletableFuture<R> executeRequest(BlizzardApiRequest<R> request)
            throws BlizzardApiClientException {

        // Make sure that this client is initialized, and if it isn't, initialize it!
        initializeIfNecessary();

        // Construct the HTTP request to the server.
        HttpRequest apiRequest = constructRequest(request);

        // Return an async future to the user that will eventually become the API response, or an error.
        return httpClient.sendAsync(apiRequest, HttpResponse.BodyHandlers.ofInputStream())
                .thenApply((HttpResponse<InputStream> response) -> evaluateResponse(apiRequest, response))
                .thenApply(HttpResponse::body)
                .thenApply((InputStream responseBodyStream) -> deserializeBody(request, responseBodyStream));
    }

    public interface BlizzardApiClientBuilder {

        BlizzardApiClientBuilder setRegion(BlizzardApiRegion region);

        BlizzardApiClientBuilder setClientId(String clientId);

        BlizzardApiClientBuilder setClientSecret(String clientSecret);

        BlizzardApiClient build();

    }

    public static class BlizzardApiClientBuilderImpl implements BlizzardApiClientBuilder {

        private static final BlizzardApiRegion DEFAULT_REGION = BlizzardApiRegion.US;
        private static final String DEFAULT_CLIENT_ID = "";
        private static final String DEFAULT_CLIENT_SECRET = "";

        private final HttpClient httpClient;
        private final ObjectMapper objectMapper;

        private BlizzardApiRegion region;
        private String clientId;
        private String clientSecret;

        public BlizzardApiClientBuilderImpl(HttpClient httpClient, ObjectMapper objectMapper) {
            this.httpClient = httpClient;
            this.objectMapper = objectMapper;
        }

        @Override
        public BlizzardApiClientBuilder setRegion(BlizzardApiRegion region) {
            this.region = region;
            return this;
        }

        @Override
        public BlizzardApiClientBuilder setClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        @Override
        public BlizzardApiClientBuilder setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        @Override
        public BlizzardApiClient build() {
            return new BlizzardApiClientImpl(
                    httpClient,
                    objectMapper,
                    region != null ? region : DEFAULT_REGION,
                    clientId != null ? clientId : DEFAULT_CLIENT_ID,
                    clientSecret != null ? clientSecret : DEFAULT_CLIENT_SECRET
            );
        }
    }

    private void initializeIfNecessary() {
        // Only one thread should perform the initialization of this client; all other threads must wait until the first
        // request finishes the initialization process before proceeding.
        if (clientState.get().equals(ClientState.UNINITIALIZED)) {

            // All threads will wait here for initialization to complete
            initializationLock.lock();

            try {
                if (clientState.get().equals(ClientState.UNINITIALIZED)) {
                    // This thread was the first to reach this point, it will perform the initialization.  Any other
                    // threads can just proceed.
                    initialize();
                }
            } finally {
                initializationLock.unlock();
            }
        }
    }

    // Only called under lock
    private void initialize() {
        refreshClientAccessToken();
        clientState.set(ClientState.RUNNING);

    }

    private void refreshClientAccessToken() {
        clientAccessToken.set(null);
    }

    private <R extends BlizzardApiResponse> HttpRequest constructRequest(BlizzardApiRequest<R> request)
            throws BlizzardApiClientException {

        try {
            // Add info common between all types of requests
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .setHeader(
                            OAUTH_TOKEN_HTTP_HEADER_NAME,
                            String.format(OAUTH_TOKEN_HTTP_HEADER_VALUE_PATTERN, clientAccessToken))
                    .uri(request.getUri(region.getFormalText(), objectMapper));


            // Handle HTTP method specific details
            switch (request.getHttpMethod()) {
                case GET:
                    requestBuilder = requestBuilder.GET();
                    break;

                case HEAD:
                    throw new BlizzardApiClientException("Unsupported operation: HEAD");

                case POST:
                    requestBuilder = requestBuilder.POST(HttpRequest.BodyPublishers.ofInputStream(() -> {
                        // ObjectMapper writes the serialized request body to bodyOutputStream, which is readable from
                        // bodyInputStream.  BodyInputStream is then provided to the HTTP library.
                        PipedInputStream bodyInputStream = new PipedInputStream();
                        try {
                            PipedOutputStream bodyOutputStream = new PipedOutputStream(bodyInputStream);
                            objectMapper.writeValue(bodyOutputStream, request);
                        } catch (JsonMappingException | JsonGenerationException e) {
                            throw new RuntimeException("Request serialization failure", e);
                        } catch (IOException e) {
                            throw new RuntimeException("Request body write failure", e);
                        }
                        return bodyInputStream;
                    }));
                    break;

                case PUT:
                    throw new BlizzardApiClientException("Unsupported operation: PUT");

                case PATCH:
                    throw new BlizzardApiClientException("Unsupported operation: PATCH");

                case DELETE:
                    throw new BlizzardApiClientException("Unsupported operation: DELETE");

                case OPTIONS:
                    throw new BlizzardApiClientException("Unsupported operation: OPTIONS");

                case TRACE:
                    throw new BlizzardApiClientException("Unsupported operation: TRACE");

            }

            // Return the constructed HTTP request.
            return requestBuilder.build();
        } catch (URISyntaxException e) {
            throw new BlizzardApiClientException("API request URI construction failed: bad URI", e);
        } catch (JsonProcessingException e) {
            throw new BlizzardApiClientException("API request URI construction failed: internal error in request", e);
        }
    }

    private HttpResponse<InputStream> evaluateResponse(HttpRequest apiRequest, HttpResponse<InputStream> response) {
        HttpStatus responseStatus = HttpStatus.resolve(response.statusCode());

        if (responseStatus == null) {
            throw new RuntimeException("Unknown HTTP response status");
        }

        switch(responseStatus) {
            case UNAUTHORIZED:
                // If the response indicated that this client was unauthorized then this client should
                // reinitialize.  Set the state to uninitialized, and then let either this http client
                // thread, another user request thread, initialize this client again (depending on which
                // happens to get there first.
                clientState.set(ClientState.UNINITIALIZED);
                initializeIfNecessary();

                // The client has been re-initialized (by this thread, or by another thread, it doesn't
                // matter).  Retry the failed request once more now that the authorization info has been
                // refreshed, and return the result of that retried request to the user no matter what.
                try {
                    return httpClient.send(apiRequest, HttpResponse.BodyHandlers.ofInputStream());
                } catch (Exception e) {
                    throw new RuntimeException("Encountered exception while retrying request", e);
                }
        }

        // The response looked good, just return it to the user.
        return response;
    }

    private <R extends BlizzardApiResponse> R deserializeBody(BlizzardApiRequest<R> request,
                                                              InputStream responseBodyStream) {
        try {
            return objectMapper.readValue(responseBodyStream, request.getResponseClass());
        } catch (IOException e) {
            throw new RuntimeException("Could not deserialize API response", e);
        }
    }

    private enum ClientState {
        UNINITIALIZED,
        RUNNING,
    }
}
