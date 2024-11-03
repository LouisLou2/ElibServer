package com.leo.elib.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TokenRes {
    VerifyFailed(1),
    TokenNotInUse(2);
    final int value;

    @JsonValue
    public int getValue() {
        return value;
    }

    @JsonCreator
    public static TokenRes valueOf(int value) {
        for (TokenRes type : TokenRes.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }
}
