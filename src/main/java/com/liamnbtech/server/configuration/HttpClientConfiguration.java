package com.liamnbtech.server.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
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

    private static final int HTTP_CONNECT_TIMEOUT_SECONDS = 10;
    private static final int HTTP_EXECUTOR_CORE_THREADS = 10;
    private static final int HTTP_EXECUTOR_MAX_THREADS = 50;
    private static final long HTTP_EXECUTOR_THREAD_KEEP_ALIVE_SECONDS = 60;
    private static final int HTTP_EXECUTOR_TASKS = 1000;

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(HTTP_CONNECT_TIMEOUT_SECONDS))
                .followRedirects(HttpClient.Redirect.NEVER)
                .sslContext(sslContext)
                .executor(httpExecutor())
                .cookieHandler(httpCookieManager())
                .build();
    }

    @Bean
    public Executor httpExecutor() {
        return new ThreadPoolExecutor(
                HTTP_EXECUTOR_CORE_THREADS,
                HTTP_EXECUTOR_MAX_THREADS,
                HTTP_EXECUTOR_THREAD_KEEP_ALIVE_SECONDS,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(HTTP_EXECUTOR_TASKS));
    }

    @Bean CookieManager httpCookieManager() {
        return new CookieManager(null, null);
    }

    public HttpClientConfiguration(SSLContext sslContext) {
        this.sslContext = sslContext;
    }
}
