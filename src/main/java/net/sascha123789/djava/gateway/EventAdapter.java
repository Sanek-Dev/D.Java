/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.gateway;

import net.sascha123789.djava.api.interactions.slash.SlashCommandUseEvent;
import net.sascha123789.djava.api.lowLevel.GatewayPacket;
import net.sascha123789.djava.gateway.events.HeartbeatEvent;
import net.sascha123789.djava.gateway.events.HelloEvent;
import net.sascha123789.djava.gateway.events.ReadyEvent;

public interface EventAdapter {
    default void onGatewayMessage(GatewayPacket packet) {

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
