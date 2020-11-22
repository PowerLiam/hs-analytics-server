package com.liamnbtech.server.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import java.net.Authenticator;
import java.net.CookieManager;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class HttpClientConfiguration {

    private final SSLContext sslContext;

    private final int httpExecutorCoreThreads = 10;
    private final int httpExecutorMaxThreads = 50;
    private final long httpExecutorThreadKeepAliveSeconds = 60;
    private final int httpExecutorTasks = 1000;

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NEVER)
                .sslContext(sslContext)
                .executor(httpExecutor())
                .cookieHandler(httpCookieManager())
                .build();
    }

    @Bean
    public Executor httpExecutor() {
        return new ThreadPoolExecutor(
                httpExecutorCoreThreads,
                httpExecutorMaxThreads,
                httpExecutorThreadKeepAliveSeconds,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(httpExecutorTasks));
    }

    @Bean CookieManager httpCookieManager() {
        return new CookieManager(null, null);
    }

    public HttpClientConfiguration(SSLContext sslContext) {
        this.sslContext = sslContext;
    }
}
