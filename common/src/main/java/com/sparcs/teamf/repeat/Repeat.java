package com.sparcs.teamf.repeat;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class Repeat {

    private static final int DEFAULT_MAX_ATTEMPTS = 10;

    private Repeat() {
    }

    public static <T> T repeat(Supplier<T> supplier,
            Predicate<T> needToRepeat,
            Supplier<RuntimeException> exceptionSupplier) throws InterruptedException {

        for (int attempt = 0; attempt < DEFAULT_MAX_ATTEMPTS; attempt++) {
            try {
                T result = supplier.get();
                if (needToRepeat.test(result)) {
                    Thread.sleep(1000);
                    continue;
                }
                return result;
            } catch (RuntimeException ignored) {
            }
        }
        throw exceptionSupplier.get();
    }
}
