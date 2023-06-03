package com.devpedrod.productapi.modules.shared.utils;

import jakarta.annotation.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class SupportUtils {

    @Nullable
    public static <T> T nullSafeGet(@Nullable Supplier<T> supplier) {
        if (supplier == null) return null;
        try {
            return supplier.get();
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static <T> Optional<T> exceptionSafeMethod(@Nullable Supplier<T> supplier) {
        if (supplier == null) return Optional.empty();
        try {
            return Optional.ofNullable(supplier.get());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static void exceptionSafeMethodVoidReturn(@Nullable NoReturnExpression method) {
        if (method == null) return;
        try {
            method.run();
        } catch (Exception ignored) {}
    }

    @FunctionalInterface
    public interface NoReturnExpression {
        void run();
    }
}
