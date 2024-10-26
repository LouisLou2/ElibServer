package com.leo.elib.comp_struct;

import com.leo.elib.constant.DeviceTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenInfo {
    //// true: access token, false: refresh token
    //boolean type;
    int userId;
    DeviceTypeEnum deviceType;
    int version;
}
