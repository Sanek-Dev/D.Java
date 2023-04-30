/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.testbot;

import net.sascha123789.djava.api.SelfUser;
import net.sascha123789.djava.api.entities.channel.*;
import net.sascha123789.djava.api.entities.reply.AllowedMentions;
import net.sascha123789.djava.api.entities.reply.MessageData;
import net.sascha123789.djava.api.enums.DiscordPermission;
import net.sascha123789.djava.api.enums.ImageType;
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

        if(msg.getContent().equals("!hello")) {
            Embed emb = new Embed.Builder()
                    .setTitle("👋 Привет 👋")
                    .setDescription("Приветствую вас!")
                    .setThumbnailUrl(event.getClient().getSelfUSer().getAvatarUrl(ImageType.JPEG))
                    .setColor(Color.decode("#39b8db"))
                    .build();

            MessageData data = new MessageData();
            data.addEmbeds(emb);
            data.addSticker(Sticker.fromId(event.getClient(), "1052543175320928267").get());

            Message m = msg.reply(data).get();
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
    public void onSlashCommandUse(SlashCommandUseEvent event) {
        TextChannel channel = event.getChannel().asTextChannel();

        MessageData data = new MessageData();
        data.setContent("@everyone");

        Embed emb = new Embed.Builder()
                .setTitle("Тест")
                .setDescription("Тест эмбед")
                .setColor(Color.decode("#34a4eb"))
                .setTimestampNow()
                .setAuthor("Sascha123789")
                .setImageUrl("https://i.imgur.com/iMZjd8w.png")
                .setThumbnailUrl("https://i.pinimg.com/originals/13/8d/52/138d52a8f429510e2c16bd67990dae3c.jpg")
                .setFooter("D.Java")
                .build();

        data.addEmbeds(emb);

        AllowedMentions mentions = AllowedMentions.create(List.of(AllowedMentions.Type.USERS));

        data.setAllowedMentions(mentions);

        Message msg = channel.sendMessage(data).get()
                .addReaction((Emoji.fromId(event.getClient(), "922404056599781397", "934011974143074314")))
                .addReaction(Emoji.fromString("✅"))
                .addReaction(Emoji.fromString("😀"));
                //.afterWait(TimeUnit.SECONDS, 5)
                //.delete("Test");
    }

    @Override
    public void onReady(ReadyEvent event) {
        SelfUser self = event.getSelfUser();
        SlashCommandOption userOpt = new SlashCommandOptionBuilder(SlashCommandOptionType.USER, "участник", "Участник").setRequired(true).build();

        SubCommand add = new SubCommandBuilder("add", "Добавить").addOptions(userOpt).build();
        SubCommandGroup user = new SubCommandGroupBuilder("user", "Участник").addSubcommand(add).build();
        SlashCommand permissions = new SlashCommandBuilder("permissions", "Права")
                .addSubcommandGroup(user).setAvailableInDm(false).build();

        SlashCommand test = new SlashCommandBuilder("test", "Тест команда").setAvailableInDm(false).build();

        SlashCommandOption msgOption = new SlashCommandOptionBuilder(SlashCommandOptionType.STRING, "текст", "Текст сообщения").setRequired(true).build();
        SlashCommand sayCmd = new SlashCommandBuilder("say", "Отправить сообщение от лица бота").setAvailableInDm(false).addOptions(msgOption).addRequiredPermission(DiscordPermission.MANAGE_MESSAGES).build();

        SlashCommandManager manager = event.getSlashCommandManager();

        manager.registerGlobalSlashCommand(permissions);
        manager.registerGlobalSlashCommand(sayCmd);
        //event.getActioner().registerGuildSlashCommand(test, "1097795359184535573");

        System.out.println(self.toString() + " is ready!");
    }
}
