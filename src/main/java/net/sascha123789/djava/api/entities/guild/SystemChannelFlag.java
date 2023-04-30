/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.guild;

import net.sascha123789.djava.api.entities.IdentifiableEnum;

public enum SystemChannelFlag implements IdentifiableEnum {
    SUPPRESS_JOIN_NOTIFICATIONS(1), SUPPRESS_PREMIUM_SUBSCRIPTIONS(1 << 1), SUPPRESS_GUILD_REMINDER_NOTIFICATIONS(1 << 2), SUPPRESS_JOIN_NOTIFICATION_REPLIES(1 << 3), SUPPRESS_ROLE_SUBSCRIPTION_PURCHASE_NOTIFICATIONS(1 << 4), SUPPRESS_ROLE_SUBSCRIPTION_PURCHASE_NOTIFICATION_REPLIES(1 << 5);
    private long code;

    SystemChannelFlag(long code) {
        this.code = code;
    }

    @Override
    public long getCode() {
        return code;
    }
}
