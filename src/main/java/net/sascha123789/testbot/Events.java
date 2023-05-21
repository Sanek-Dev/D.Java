/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.testbot;

import net.sascha123789.djava.api.SelfUser;
import net.sascha123789.djava.api.User;
import net.sascha123789.djava.api.entities.channel.Embed;
import net.sascha123789.djava.api.entities.channel.MessageableChannel;
import net.sascha123789.djava.api.entities.guild.Guild;
import net.sascha123789.djava.api.entities.guild.Member;
import net.sascha123789.djava.api.entities.role.Role;
import net.sascha123789.djava.api.enums.ChannelType;
import net.sascha123789.djava.api.enums.DiscordPermission;
import net.sascha123789.djava.api.enums.SlashCommandOptionType;
import net.sascha123789.djava.api.interactions.DeferEvent;
import net.sascha123789.djava.api.interactions.slash.*;
import net.sascha123789.djava.api.managers.SlashCommandManager;
import net.sascha123789.djava.gateway.EventAdapter;
import net.sascha123789.djava.gateway.events.HeartbeatEvent;
import net.sascha123789.djava.gateway.events.HelloEvent;
import net.sascha123789.djava.gateway.events.ReadyEvent;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Events implements EventAdapter {

    @Override
    @DeferEvent
    public void onSlashCommandUse(SlashCommandUseEvent event) {
        Guild guild = event.getGuild();
        String name = event.getName();

        switch (name) {
            case "say" -> {
                String text = event.getOptions().get("текст").getValueAsString().get();
                Embed emb = new Embed.Builder()
                        .setTitle("Успешно!")
                        .setColor(new Color(46, 154, 217))
                        .setDescription("Название сервера: " + guild.getName())
                        .setThumbnailUrl(guild.getIconUrl())
                        .setTimestampNow()
                        .build();

                event.editReply(emb);
                event.replyFollowup(text);
            }
            case "calc" -> {
                int num0 = event.getOptions().get("число1").getValueAsInt().get();
                int num1 = event.getOptions().get("число2").getValueAsInt().get();
                int res = num0 + num1;

                Embed emb = new Embed.Builder()
                        .setTitle("Результат")
                        .setDescription(num0 + " + " + num1 + " = " + res)
                        .setColor(new Color(46, 154, 217))
                        .setThumbnailUrl(event.getClient().getSelfUser().getAvatarUrl())
                        .setImageUrl(guild.getIconUrl())
                        .build();

                event.editReply(emb);
            }
            case "permissions" -> {
                Member member = event.getOptions().get("участник").getValueAsMember().get();
                User user = event.getOptions().get("участник").getValueAsUser().get();
                event.editReply("Привет, *" + member.getNickname() + "*!\nИмя аккаунта: *" + user.getUsername() + "*");
            }
            case "send-msg" -> {
                MessageableChannel channel = event.getOptions().get("канал").getValueAsChannel().get().asMessageable();
                String msg = event.getOptions().get("текст").getValueAsString().get();
                channel.sendMessage(msg);
                event.editReply("Успешно!");
            }
            case "role" -> {
                event.editReply("Успешно!");
                String sub = event.getSubcommandName();
                Member member = event.getOptions().get("участник").getValueAsMember().get();
                Role role = event.getOptions().get("роль").getValueAsRole().get();

                if (sub.equals("add")) {
                    member.addRole(role);
                } else if (sub.equals("remove")) {
                    member.removeRole(role);
                }
            }
            case "bot-info" -> {
                Embed emb = new Embed.Builder()
                        .setTitle("🤖 Информация о боте 🤖")
                        .setThumbnailUrl(event.getClient().getSelfUser().getAvatarUrl())
                        .addField("Кол-во серверов", String.valueOf(event.getClient().getGuilds().size()))
                        .setColor(Color.YELLOW)
                        .build();

                event.editReply(emb);
            }
            case "timeout" -> {
                Member member = event.getOptions().get("участник").getValueAsMember().get();
                member.timeout(TimeUnit.DAYS, 1);

                event.editReply("Успешно!");
            }
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

        SlashCommandOption channelOpt = new SlashCommandOption.Builder(SlashCommandOptionType.CHANNEL, "канал", "Канал сервера")
                .setRequired(true)
                .setChannelTypes(ChannelType.TEXT).build();
        SlashCommand send = manager.createBuilder("send-msg", "Отправить сообщение в канал")
                .addOptions(channelOpt, msgOption).build();

        SlashCommandOption roleOpt = new SlashCommandOption.Builder(SlashCommandOptionType.ROLE, "роль", "Роль").setRequired(true).build();
        SubCommand addSub = new SubCommand.Builder("add", "Добавить роль").addOptions(userOpt, roleOpt).build();
        SubCommand removeSub = new SubCommand.Builder("remove", "Убрать роль").addOptions(userOpt, roleOpt).build();
        SlashCommand role = manager.createBuilder("role", "Роли")
                .addSubcommand(addSub)
                .addSubcommand(removeSub).setAvailableInDm(false).build();

        SlashCommand botInfo = manager.createBuilder("bot-info", "Информация о боте").setAvailableInDm(true).build();

        SlashCommand timeout = manager.createBuilder("timeout", "Таймаут участника")
                .setAvailableInDm(false)
                .addOptions(new SlashCommandOption.Builder(SlashCommandOptionType.USER, "участник", "Участник сервера").setRequired(true).build()).build();

        manager.bulkOverwriteGlobalSlashCommands(permissions, sayCmd, calc, send, role, botInfo, timeout);

        System.out.println(self.toString() + " is ready!");
    }
}
