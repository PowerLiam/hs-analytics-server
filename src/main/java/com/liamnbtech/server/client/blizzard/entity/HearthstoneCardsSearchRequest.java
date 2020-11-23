package com.liamnbtech.server.client.blizzard.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liamnbtech.server.client.blizzard.BlizzardApiRegion;
import com.liamnbtech.server.utils.io.UrlUtils;
import org.springframework.http.HttpMethod;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HearthstoneCardsSearchRequest implements BlizzardApiRequest<HearthstoneCardsSearchResponse> {
    private static final String REQUEST_SCHEME = "HTTPS";
    private static final String REQUEST_HOST_PATTERN = "%s.api.blizzard.com";
    private static final String REQUEST_PATH = "/hearthstone/cards";
    private static final String REQUEST_LIST_DELIMITER = ",";

    @JsonProperty(":region")
    private final BlizzardApiRegion region;
    private final String locale;
    private final String set;
    @JsonProperty("class")
    private final String clazz;
    private final String manaCost;
    private final String attack;
    private final String health;
    private final String collectible;
    private final String rarity;
    private final String type;
    private final String minionType;
    private final String keyword;
    private final String textFilter;
    private final String gameMode;
    private final String page;
    private final String pageSize;
    private final String sort;
    private final String order;

    @JsonCreator
    public HearthstoneCardsSearchRequest(
            BlizzardApiRegion region,
            String locale,
            String set,
            String clazz,
            String manaCost,
            String attack,
            String health,
            String collectible,
            String rarity,
            String type,
            String minionType,
            String keyword,
            String textFilter,
            String gameMode,
            String page,
            String pageSize,
            String sort,
            String order) {

        this.region = region;
        this.locale = locale;
        this.set = set;
        this.clazz = clazz;
        this.manaCost = manaCost;
        this.attack = attack;
        this.health = health;
        this.collectible = collectible;
        this.rarity = rarity;
        this.type = type;
        this.minionType = minionType;
        this.keyword = keyword;
        this.textFilter = textFilter;
        this.gameMode = gameMode;
        this.page = page;
        this.pageSize = pageSize;
        this.sort = sort;
        this.order = order;
    }

    @Override
    public URI getUri(String region, ObjectMapper objectMapper)
            throws URISyntaxException, JsonProcessingException {

        return new URI(
                REQUEST_SCHEME,
                String.format(
                        REQUEST_HOST_PATTERN,
                        this.region != null ? this.region : region),
                REQUEST_PATH,
                constructQueryString(objectMapper),
                null
        );
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public Class<HearthstoneCardsSearchResponse> getResponseClass() {
        return HearthstoneCardsSearchResponse.class;
    }

    public interface HearthstoneCardsSearchRequestBuilder {

        HearthstoneCardsSearchRequestBuilder setLocale(Locale locale);

        HearthstoneCardsSearchRequestBuilder setSet(String set);

        HearthstoneCardsSearchRequestBuilder setClass(String clazz);

        HearthstoneCardsSearchRequestBuilder setManaCost(List<String> manaCost);

        HearthstoneCardsSearchRequestBuilder setAttack(List<String> attack);

        HearthstoneCardsSearchRequestBuilder setHealth(List<String> health);

        HearthstoneCardsSearchRequestBuilder setCollectible(List<String> collectible);

        HearthstoneCardsSearchRequestBuilder setRarity(String rarity);

        HearthstoneCardsSearchRequestBuilder setType(String type);

        HearthstoneCardsSearchRequestBuilder setMinionType(String minionType);

        HearthstoneCardsSearchRequestBuilder setKeyword(String keyword);

        HearthstoneCardsSearchRequestBuilder setTextFilter(String textFilter);

        HearthstoneCardsSearchRequestBuilder setGameMode(String gameMode);

        HearthstoneCardsSearchRequestBuilder setPage(String page);

        HearthstoneCardsSearchRequestBuilder setPageSize(String pageSize);

        HearthstoneCardsSearchRequestBuilder setSort(String sort);

        HearthstoneCardsSearchRequestBuilder setOrder(String order);

        HearthstoneCardsSearchRequest build();
    }

    public static class HearthstoneCardsSearchRequestBuilderImpl implements HearthstoneCardsSearchRequestBuilder {
        private final BlizzardApiRegion region;

        private Locale locale;
        private String set;
        private String clazz;
        private List<String> manaCost;
        private List<String> attack;
        private List<String> health;
        private List<String> collectible;
        private String rarity;
        private String type;
        private String minionType;
        private String keyword;
        private String textFilter;
        private String gameMode;
        private String page;
        private String pageSize;
        private String sort;
        private String order;


        public HearthstoneCardsSearchRequestBuilderImpl(BlizzardApiRegion region) {
            this.region = region;
        }

        @Override
        public HearthstoneCardsSearchRequestBuilder setLocale(Locale locale) {
            this.locale = locale;
            return this;
        }

        @Override
        public HearthstoneCardsSearchRequestBuilder setSet(String set) {
            this.set = set;
            return this;
        }

        @Override
        public HearthstoneCardsSearchRequestBuilder setClass(String clazz) {
            this.clazz = clazz;
            return this;
        }

        @Override
        public HearthstoneCardsSearchRequestBuilder setManaCost(List<String> manaCost) {
            this.manaCost = manaCost;
            return this;
        }

        @Override
        public HearthstoneCardsSearchRequestBuilder setAttack(List<String> attack) {
            this.attack = attack;
            return this;
        }

        @Override
        public HearthstoneCardsSearchRequestBuilder setHealth(List<String> health) {
            this.health = health;
            return this;
        }

        @Override
        public HearthstoneCardsSearchRequestBuilder setCollectible(List<String> collectible) {
            this.collectible = collectible;
            return this;
        }

        @Override
        public HearthstoneCardsSearchRequestBuilder setRarity(String rarity) {
            this.rarity = rarity;
            return this;
        }

        @Override
        public HearthstoneCardsSearchRequestBuilder setType(String type) {
            this.type = type;
            return this;
        }

        @Override
        public HearthstoneCardsSearchRequestBuilder setMinionType(String minionType) {
            this.minionType = minionType;
            return this;
        }

        @Override
        public HearthstoneCardsSearchRequestBuilder setKeyword(String keyword) {
            this.keyword = keyword;
            return this;
        }

        @Override
        public HearthstoneCardsSearchRequestBuilder setTextFilter(String textFilter) {
            this.textFilter = textFilter;
            return this;
        }

        @Override
        public HearthstoneCardsSearchRequestBuilder setGameMode(String gameMode) {
            this.gameMode = gameMode;
            return this;
        }

        @Override
        public HearthstoneCardsSearchRequestBuilder setPage(String page) {
            this.page = page;
            return this;
        }

        @Override
        public HearthstoneCardsSearchRequestBuilder setPageSize(String pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        @Override
        public HearthstoneCardsSearchRequestBuilder setSort(String sort) {
            this.sort = sort;
            return this;
        }

        @Override
        public HearthstoneCardsSearchRequestBuilder setOrder(String order) {
            this.order = order;
            return this;
        }

        @Override
        public HearthstoneCardsSearchRequest build() {
            return new HearthstoneCardsSearchRequest(
                    region,
                    locale == null ? null : locale.getDisplayName(),
                    set,
                    clazz,
                    manaCost == null ? null : String.join(REQUEST_LIST_DELIMITER, manaCost),
                    attack == null ? null : String.join(REQUEST_LIST_DELIMITER, attack),
                    health == null ? null : String.join(REQUEST_LIST_DELIMITER, health),
                    collectible == null ? null : String.join(REQUEST_LIST_DELIMITER, collectible),
                    rarity,
                    type,
                    minionType,
                    keyword,
                    textFilter,
                    gameMode,
                    page,
                    pageSize,
                    sort,
                    order);
        }
    }

    private String constructQueryString(ObjectMapper objectMapper) throws JsonProcessingException {
        List<String> queryParamStrings = new ArrayList<>();
        JsonNode objectRoot = objectMapper.readTree(objectMapper.writeValueAsString(this));

        for (JsonNode objectField : objectRoot) {
            String fieldName = objectField.asText();
            JsonNode fieldValue = objectRoot.get(fieldName);

            if (fieldValue != null && !fieldValue.isNull()) {
                queryParamStrings.add(UrlUtils.toQueryParamString(fieldName,fieldValue.asText()));
            }
        }

        return UrlUtils.toQueryString(queryParamStrings);
    }
}
