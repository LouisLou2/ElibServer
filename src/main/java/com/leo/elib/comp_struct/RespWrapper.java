package com.leo.elib.comp_struct;

import com.leo.elib.constant.ResCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RespWrapper<T> {
    private int code;
    private final String message;
    private final T data;

    public static final RespWrapper<?> SUCCESS = new RespWrapper<>(ResCodeEnum.Success.getCode(), null, null);
    
    public static <T> RespWrapper<T> success(T data) {
        return new RespWrapper<>(ResCodeEnum.Success.getCode(), null, data);
    }
    
    public static RespWrapper<?> error(ResCodeEnum resCodeEnum) {
        return new RespWrapper<>(resCodeEnum.getCode(), resCodeEnum.getMsg(), null);
    }

    public static RespWrapper<?> error(ResCodeEnum resCodeEnum, String detail) {
        return new RespWrapper<>(resCodeEnum.getCode(), resCodeEnum.getMsg() + ": " + detail, null);
    }
    
}
