/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.testbot;

import net.sascha123789.djava.api.SelfUser;
import net.sascha123789.djava.api.entities.channel.Embed;
import net.sascha123789.djava.api.entities.guild.Guild;
import net.sascha123789.djava.api.enums.DiscordPermission;
import net.sascha123789.djava.api.enums.SlashCommandOptionType;
import net.sascha123789.djava.api.interactions.slash.*;
import net.sascha123789.djava.api.managers.SlashCommandManager;
import net.sascha123789.djava.gateway.EventAdapter;
import net.sascha123789.djava.gateway.events.HeartbeatEvent;
import net.sascha123789.djava.gateway.events.HelloEvent;
import net.sascha123789.djava.gateway.events.ReadyEvent;

import java.awt.*;

public class Events implements EventAdapter {
    @Override
    public void onSlashCommandUse(SlashCommandUseEvent event) {
        Guild guild = event.getGuild();
        String name = event.getName();

        if(name.equals("say")) {
            String text = event.getOptions().get("текст").getValueAsString();

            Embed emb = new Embed.Builder()
                    .setTitle("Успешно!")
                    .setColor(new Color(46, 154, 217))
                    .setDescription("Название сервера: " + guild.getName())
                    .setFooter("/" + event.getName() + " " + event.getSubcommandGroupName() + " " + event.getSubcommandName())
                    .setThumbnailUrl(guild.getIconUrl())
                    .build();

            event.reply(true, emb);
            event.replyFollowup(text);
        } else if(name.equals("calc")) {
            int num0 = event.getOptions().get("число1").getValueAsInt();
            int num1 = event.getOptions().get("число2").getValueAsInt();
            int res = num0 + num1;

            event.reply(num0 + " + " + num1 + " = " + res);
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

        SlashCommandOption num0 = new SlashCommandOption.Builder(SlashCommandOptionType.INTEGER, "число1", "Первое число").setRequired(true).build();
        SlashCommandOption num1 = new SlashCommandOption.Builder(SlashCommandOptionType.INTEGER, "число2", "Второе число").setRequired(true).build();
        SlashCommand calc = manager.createBuilder("calc", "Посчитать значение чисел").addOptions(num0, num1).build();

        manager.bulkOverwriteGlobalSlashCommands(permissions, sayCmd, calc);

        System.out.println(self.toString() + " is ready!");
    }
}
