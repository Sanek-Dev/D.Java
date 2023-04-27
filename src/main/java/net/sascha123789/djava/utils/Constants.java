package net.sascha123789.djava.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.sascha123789.djava.api.User;
import net.sascha123789.djava.api.channel.TextChannel;
import net.sascha123789.djava.api.channel.VoiceChannel;
import net.sascha123789.djava.api.interactions.slash.SlashCommand;
import net.sascha123789.djava.api.interactions.slash.SlashCommandOption;
import net.sascha123789.djava.api.interactions.slash.SubCommand;
import net.sascha123789.djava.api.interactions.slash.SubCommandGroup;
import net.sascha123789.djava.api.json.*;

public class Constants {
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(User.class, new UserSerializer())
            .registerTypeAdapter(User.class, new UserDeserializer())
            .registerTypeAdapter(SlashCommand.class, new SlashCommandDeserializer())
            .registerTypeAdapter(SlashCommand.class, new SlashCommandSerializer())
            .registerTypeAdapter(SubCommand.class, new SubCommandSerializer())
            .registerTypeAdapter(SubCommandGroup.class, new SubCommandGroupSerializer())
            .registerTypeAdapter(SlashCommandOption.class, new SlashCommandOptionSerializer())
            .registerTypeAdapter(TextChannel.class, new TextChannelDeserializer())
            .registerTypeAdapter(VoiceChannel.class, new VoiceChannelDeserializer())
            .create();
    public static final String BASE_URL = "https://discord.com/api/v10";
    public static final String BASE_IMAGES_URL = "https://cdn.discordapp.com/";
    public static final String USER_AGENT = "DiscordBot";
}
