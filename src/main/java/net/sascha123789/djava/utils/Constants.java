package net.sascha123789.djava.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.sascha123789.djava.api.User;
import net.sascha123789.djava.api.entities.guild.Guild;
import net.sascha123789.djava.api.interactions.slash.SlashCommand;
import net.sascha123789.djava.api.interactions.slash.SlashCommandOption;
import net.sascha123789.djava.api.interactions.slash.SubCommand;
import net.sascha123789.djava.api.interactions.slash.SubCommandGroup;
import net.sascha123789.djava.api.json.*;

public class Constants {
    public static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(SlashCommand.class, new SlashCommandTypeAdapter())
            .registerTypeAdapter(SubCommand.class, new SubCommandTypeAdapter())
            .registerTypeAdapter(SubCommandGroup.class, new SubCommandGroupTypeAdapter())
            .registerTypeAdapter(SlashCommandOption.class, new SlashCommandOptionTypeAdapter())
            .create();

    public static final ObjectMapper MAPPER = new ObjectMapper();

    public static final String BASE_URL = "https://discord.com/api/v10";
    public static final String BASE_IMAGES_URL = "https://cdn.discordapp.com/";
    public static final String USER_AGENT = "DiscordBot(D.JAVA; v1)";
}
