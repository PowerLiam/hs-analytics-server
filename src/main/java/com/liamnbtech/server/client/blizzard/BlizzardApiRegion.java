package com.liamnbtech.server.client.blizzard;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum BlizzardApiRegion {
    US("us"),
    EU("eu"),
    KR("kr"),
    TW("tw");

    private static Map<String, BlizzardApiRegion> enumConstantsByFormalText = new HashMap<String, BlizzardApiRegion>();

    static {
        for (BlizzardApiRegion apiRegion : values()) {
            enumConstantsByFormalText.put(apiRegion.getFormalText(), apiRegion);
        }
    }

    private final String formalText;

    BlizzardApiRegion(String formalText) {
        this.formalText = formalText;
    }

    @JsonValue
    public String getFormalText() {
        return formalText;
    }

    @JsonCreator
    public static BlizzardApiRegion fromFormatText(String formalText) {
        for (String candidateFormalText : enumConstantsByFormalText.keySet()) {
            if (candidateFormalText.equals(formalText)) {
                return enumConstantsByFormalText.get(candidateFormalText);
            }
        }
        throw new IllegalArgumentException(String.format("No enum constant for provided formal text: %s", formalText));
    }

}
