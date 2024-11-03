package com.leo.elib.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeviceTypeEnum {
    Mobile(1),
    PC(2),
    Pad(3),
    Web(4);
    
    final int value;

    @JsonValue
    public int getValue() {
        return value;
    }

    @JsonCreator
    public static DeviceTypeEnum valueOf(int value) {
        for (DeviceTypeEnum type : DeviceTypeEnum.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }
}
