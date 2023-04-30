/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.gateway;

import net.sascha123789.djava.api.interactions.slash.SlashCommandUseEvent;
import net.sascha123789.djava.gateway.events.*;

public interface EventAdapter {
    default void onMessageReactionRemoveAllEmoji(MessageReactionRemoveAllEmojiEvent event) {

    }

    default void onMessageReactionRemoveAll(MessageReactionRemoveAllEvent event) {

    }

    default void onMessageReactionRemove(MessageReactionRemoveEvent event) {

    }

    default void onMessageReactionAdd(MessageReactionAddEvent event) {

    }

    default void onMessageBulkDelete(MessageDeleteBulkEvent event) {

    }

    default void onMessageDelete(MessageDeleteEvent event) {

    }

    default void onMessageUpdate(MessageUpdateEvent event) {

    }

    default void onMessageCreate(MessageCreateEvent event) {

    }

    default void onSlashCommandUse(SlashCommandUseEvent event) {

    }

    default void onHello(HelloEvent event) {

    }

    default void onHeartbeat(HeartbeatEvent event) {

    }

    default void onReady(ReadyEvent event) {

    }
}
