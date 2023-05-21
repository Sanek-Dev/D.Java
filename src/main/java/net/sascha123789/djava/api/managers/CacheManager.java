/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.managers;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.sascha123789.djava.api.entities.channel.BaseChannel;
import net.sascha123789.djava.api.entities.guild.Guild;
import net.sascha123789.djava.api.entities.guild.Member;
import net.sascha123789.djava.gateway.DiscordClient;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class CacheManager implements DiscordManager {
    private final LoadingCache<String, Guild> guildCache;
    private final LoadingCache<String, Member> memberCache;
    private final LoadingCache<String, BaseChannel> channelCache;

    public CacheManager(DiscordClient client) {
        this.guildCache = CacheBuilder.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Guild>() {
                    @NotNull
                    @Override
                    public Guild load(@NotNull String key) throws Exception {
                        return client.getGuildById(key).get();
                    }
                });

        this.memberCache = CacheBuilder.newBuilder()
                .expireAfterWrite(2, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Member>() {
                    @NotNull
                    @Override
                    public Member load(@NotNull String key) throws Exception {
                        String[] arr = key.split(":");

                        return guildCache.getUnchecked(arr[0]).getMemberById(arr[1]);
                    }
                });

        this.channelCache = CacheBuilder.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build(new CacheLoader<String, BaseChannel>() {
                    @Override
                    public BaseChannel load(String key) throws Exception {
                        return client.getChannelById(key).get();
                    }
                });
    }

    public LoadingCache<String, BaseChannel> getChannelCache() {
        return channelCache;
    }

    public LoadingCache<String, Guild> getGuildCache() {
        return guildCache;
    }

    /**
     * Key format: guildId,memberId**/
    public LoadingCache<String, Member> getMemberCache() {
        return memberCache;
    }
}
