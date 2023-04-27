package net.sascha123789.djava.utils;

public class DiscordAPIException extends RuntimeException {
    public DiscordAPIException(String msg) {
        super(msg);
    }

    public DiscordAPIException() {
        super();
    }
}
