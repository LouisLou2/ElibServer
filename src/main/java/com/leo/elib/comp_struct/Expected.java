package com.leo.elib.comp_struct;

import lombok.Getter;

@Getter
public class Expected<T, E> {
    private final T value;
    private final E error;
    private final boolean isSuccess;

    private Expected(T value, E error, boolean isSuccess) {
        this.value = value;
        this.error = error;
        this.isSuccess = isSuccess;
    }

    public static <T, E> Expected<T, E> success(T value) {
        return new Expected<>(value, null, true);
    }

    public static <T, E> Expected<T, E> error(E error) {
        return new Expected<>(null, error, false);
    }
}
