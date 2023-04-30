/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.channel;

import net.sascha123789.djava.api.entities.IdentifiableEnum;

public enum MessageFlag implements IdentifiableEnum {
    CROSSPOST(1), IS_CROSSPOST(1 << 1), SUPPRESS_EMBEDS(1 << 2), SOURCE_MESSAGE_DELETED(1 << 3), URGENT(1 << 4), HAS_THREAD(1 << 5), EPHEMERAL(1 << 6), LOADING(1 << 7), FAILED_TO_MENTION_SOME_ROLES_IN_THREAD(1 << 8), SUPPRESS_NOTIFICATIONS(1 << 12), IS_VOICE_MESSAGE(1 << 13);

    private long code;

    MessageFlag(long code) {
        this.code = code;
    }

    @Override
    public long getCode() {
        return code;
    }
}
