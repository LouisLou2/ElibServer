package com.leo.elib.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TokenRes {
    VerifyFailed(1),
    TokenNotInUse(2);
    int value;
}
