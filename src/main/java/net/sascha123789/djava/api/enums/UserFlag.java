/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.enums;

public enum UserFlag {
    STAFF(1), PARTNER(1 << 1), HYPESQUAD(1 << 2), BUG_HUNTER_LEVEL_1(1 << 3), HYPESQUAD_BRAVERY(1 << 6), HYPESQUAD_BRILLIANCE(1 << 7), HYPESQUAD_BALANCE(1 << 8), EARLY_SUPPORTER(1 << 9), TEAM_PSEUDO_USER(1 << 10), BUG_HUNTER_LEVEL_2(1 << 14), VERIFIED_BOT(1 << 16), VERIFIED_DEVELOPER(1 << 17), CERTIFIED_MODERATOR(1 << 18), BOT_HTTP_INTERACTIONS(1 << 19), ACTIVE_DEVELOPER(1 << 22);

    private long code;

    UserFlag(long code) {
        this.code = code;
    }

    public long getCode() {
        return this.code;
    }
}
