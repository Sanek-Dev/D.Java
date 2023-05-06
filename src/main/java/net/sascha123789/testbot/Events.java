/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.testbot;

import net.sascha123789.djava.api.SelfUser;
import net.sascha123789.djava.api.entities.channel.*;
import net.sascha123789.djava.api.entities.guild.Guild;
import net.sascha123789.djava.api.entities.reply.AllowedMentions;
import net.sascha123789.djava.api.entities.reply.MessageData;
import net.sascha123789.djava.api.enums.DiscordPermission;
import net.sascha123789.djava.api.enums.SlashCommandOptionType;
import net.sascha123789.djava.api.interactions.slash.*;
import net.sascha123789.djava.api.managers.SlashCommandManager;
import net.sascha123789.djava.gateway.EventAdapter;
import net.sascha123789.djava.gateway.events.HeartbeatEvent;
import net.sascha123789.djava.gateway.events.HelloEvent;
import net.sascha123789.djava.gateway.events.MessageCreateEvent;
import net.sascha123789.djava.gateway.events.ReadyEvent;

import java.awt.*;
import java.util.List;

public class Events implements EventAdapter {

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        Message msg = event.getMessage();
        MessageableChannel channel = event.getChannel().asMessageable();
        Guild guild = event.getGuild();

        if(msg.getContent().equals("!hello")) {
            Embed emb = new Embed.Builder()
                    .setColor(Color.decode("#3887d6"))
                    .setThumbnailUrl(event.getClient().getSelfUSer().getAvatarUrl())
                    .setDescription("Всего серверов у бота: *" + event.getClient().getGuilds().size() + "*")
                    .setTitle("Привет!")
                    .setTimestampNow()
                    .build();

            msg.reply(emb);
        }
    }

    @Override
    public void onHello(HelloEvent event) {
        System.out.println("Hello!");
    }

    @Override
    public void onHeartbeat(HeartbeatEvent event) {
        System.out.println("Heartbeat!");
    }


    @Override
    public void onReady(ReadyEvent event) {
        SelfUser self = event.getSelfUser();
        SlashCommandManager manager = event.getSlashCommandManager();
        SlashCommandOption userOpt = new SlashCommandOption.Builder(SlashCommandOptionType.USER, "участник", "Участник").setRequired(true).build();

        SubCommand add = new SubCommand.Builder("add", "Добавить").addOptions(userOpt).build();
        SubCommandGroup user = new SubCommandGroup.Builder("user", "Участник").addSubcommand(add).build();
        SlashCommand permissions = manager.createBuilder("permissions", "Права")
                .addSubcommandGroup(user).setAvailableInDm(false).build();

        SlashCommandOption msgOption = new SlashCommandOption.Builder(SlashCommandOptionType.STRING, "текст", "Текст сообщения").setRequired(true).build();
        SlashCommand sayCmd = manager.createBuilder("say", "Отправить сообщение от лица бота").setAvailableInDm(false).addOptions(msgOption).addRequiredPermission(DiscordPermission.MANAGE_MESSAGES).build();

        manager.registerGlobalSlashCommand(permissions);
        manager.registerGlobalSlashCommand(sayCmd);

        System.out.println(self.toString() + " is ready!");
    }
}
