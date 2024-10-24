package com.leo.elib.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeviceTypeEnum {
    Mobile(1),
    PC(2),
    Pad(3),
    Web(4);
    
    int value;
    
    public static DeviceTypeEnum valueOf(int value) {
        for (DeviceTypeEnum type : DeviceTypeEnum.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }
}
