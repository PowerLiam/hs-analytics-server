package com.liamnbtech.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liamnbtech.server.client.blizzard.BlizzardApiClient;
import com.liamnbtech.server.client.blizzard.BlizzardApiClientException;
import com.liamnbtech.server.client.blizzard.BlizzardApiClientImpl;
import com.liamnbtech.server.client.blizzard.BlizzardApiRegion;
import com.liamnbtech.server.client.blizzard.entity.BlizzardApiRequest;
import com.liamnbtech.server.client.blizzard.entity.HearthstoneCardsSearchRequest;
import com.liamnbtech.server.client.blizzard.entity.HearthstoneCardsSearchResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

import java.net.http.HttpClient;
import java.util.concurrent.ExecutionException;

@SpringBootTest
class ServerApplicationTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpClient httpClient;



    @Test
    void contextLoads() {

    }

    @Test
    void obtainHearthstoneCards() throws BlizzardApiClientException, ExecutionException, InterruptedException {
        BlizzardApiRegion region = BlizzardApiRegion.US;

        BlizzardApiClient apiClient = new BlizzardApiClientImpl.BlizzardApiClientBuilderImpl(httpClient, objectMapper)
                .setRegion(region)
                .setClientId("543f2cdafd7e44e8b35778daeb075433")
                .setClientSecret("")
                .build();

        BlizzardApiRequest<HearthstoneCardsSearchResponse> requestForCards =
                new HearthstoneCardsSearchRequest.HearthstoneCardsSearchRequestBuilderImpl(region)
                .build();

        HearthstoneCardsSearchResponse response = apiClient.executeRequest(requestForCards).get();
    }
}
