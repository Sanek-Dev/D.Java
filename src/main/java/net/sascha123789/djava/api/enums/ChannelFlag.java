/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.enums;

public enum ChannelFlag {
    PINNED(1 << 1), REQUIRE_TAG(1 << 4);

    private long code;

    ChannelFlag(long code) {
        this.code = code;
    }

    public long getCode() {
        return code;
    }
}
