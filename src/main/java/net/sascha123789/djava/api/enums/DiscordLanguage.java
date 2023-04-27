/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.enums;

public enum DiscordLanguage {
    INDONESIAN("id"), DANISH("da"), GERMAN("de"), ENGLISH_UK("en-GB"), ENGLISH_US("en-US"), SPANISH("es-ES"), FRENCH("fr"), CROATIAN("hr"), ITALIAN("it"), LITHUANIAN("lt"), HUNGARIAN("hu"), DUTCH("nl"), NORWEGIAN("no"), POLISH("pl"), PORTUGUESE_BRAZILIAN("pt-BR"), ROMANIAN_ROMANIA("ro"), FINNISH("fi"), SWEDISH("sv-SE"), VIETNAMESE("vi"), TURKISH("tr"), CZECH("cs"), GREEK("el"), BULGARIAN("bg"), RUSSIAN("ru"), UKRAINIAN("uk"), HINDI("hi"), THAI("th"), CHINESE_CHINA("zh-CN"), JAPANESE("ja"), CHINESE_TAIWAN("zh-TW"), KOREAN("ko");

    private String id;

    DiscordLanguage(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
