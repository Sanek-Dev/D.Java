/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.testbot;

import net.sascha123789.djava.api.User;
import net.sascha123789.djava.api.channel.TextChannel;
import net.sascha123789.djava.api.channel.TextChannelUpdater;
import net.sascha123789.djava.api.enums.DiscordPermission;
import net.sascha123789.djava.api.enums.SlashCommandOptionType;
import net.sascha123789.djava.api.interactions.slash.*;
import net.sascha123789.djava.gateway.EventAdapter;
import net.sascha123789.djava.gateway.events.HeartbeatEvent;
import net.sascha123789.djava.gateway.events.HelloEvent;
import net.sascha123789.djava.gateway.events.ReadyEvent;

public class Events implements EventAdapter {
    @Override
    public void onHello(HelloEvent event) {
        System.out.println("Hello!");
    }

    @Override
    public void onHeartbeat(HeartbeatEvent event) {
        System.out.println("Heartbeat!");
    }

    @Override
    public void onSlashCommandUse(SlashCommandUseEvent event) {
        TextChannel channel = event.getChannel().getAsTextChannel();
    }

    @Override
    public void onReady(ReadyEvent event) {
        User self = event.getSelfUser();
        SlashCommandOption userOpt = new SlashCommandOptionBuilder(SlashCommandOptionType.USER, "участник", "Участник").setRequired(true).build();

        SubCommand add = new SubCommandBuilder("add", "Добавить").addOptions(userOpt).build();
        SubCommandGroup user = new SubCommandGroupBuilder("user", "Участник").addSubcommand(add).build();
        SlashCommand permissions = new SlashCommandBuilder("permissions", "Права")
                .addSubcommandGroup(user).setAvailableInDm(false).build();

        SlashCommand test = new SlashCommandBuilder("test", "Тест команда").setAvailableInDm(false).build();

        SlashCommandOption msgOption = new SlashCommandOptionBuilder(SlashCommandOptionType.STRING, "текст", "Текст сообщения").setRequired(true).build();
        SlashCommand sayCmd = new SlashCommandBuilder("say", "Отправить сообщение от лица бота").setAvailableInDm(false).addOptions(msgOption).addRequiredPermission(DiscordPermission.MANAGE_MESSAGES).build();

        event.getActioner().registerGlobalSlashCommand(permissions);
        event.getActioner().registerGlobalSlashCommand(sayCmd);
        event.getActioner().registerGuildSlashCommand(test, "1097795359184535573");

        System.out.println(self.toString() + " is ready!");
    }
}
