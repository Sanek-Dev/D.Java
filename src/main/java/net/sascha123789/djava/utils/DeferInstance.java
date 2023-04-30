/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.utils;

import java.util.concurrent.TimeUnit;

public interface DeferInstance<T> {
    default T afterWait(TimeUnit unit, long n) {
        try {
            unit.sleep(n);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return ((T) this);
    }
}
