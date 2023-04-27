/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.ipc;

import net.sascha123789.djava.api.User;

public interface IPCEventListener {
    void onReady(DiscordIPC client);

    default void onAuth(DiscordIPC client, User user, String accessToken) {

    }

    default void onPackageSent(IPCPackage ipcPackage) {

    }
}
