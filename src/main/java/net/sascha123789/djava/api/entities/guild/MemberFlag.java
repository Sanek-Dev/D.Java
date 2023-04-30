/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.guild;

import net.sascha123789.djava.api.entities.IdentifiableEnum;

public enum MemberFlag implements IdentifiableEnum {
    DID_REJOIN(1), COMPLETED_ONBOARDING(1 << 1), BYPASSES_VERIFICATION(1 << 2), STARTED_ONBOARDING(1 << 3);

    private long code;

    MemberFlag(long code) {
        this.code = code;
    }

    @Override
    public long getCode() {
        return code;
    }
}
