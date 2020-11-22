package com.liamnbtech.server.client.blizzard;

import com.liamnbtech.server.client.blizzard.entity.BlizzardApiRequest;
import com.liamnbtech.server.client.blizzard.entity.BlizzardApiResponse;

import java.util.concurrent.CompletableFuture;

public interface BlizzardApiClient {

    <R extends BlizzardApiResponse> CompletableFuture<R> executeRequest(BlizzardApiRequest<R> request) throws BlizzardApiClientException;

}
