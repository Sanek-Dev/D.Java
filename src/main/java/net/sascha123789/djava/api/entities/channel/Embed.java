/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.channel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.sascha123789.djava.utils.Constants;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.util.HashSet;
import java.util.Set;

public class Embed {
    private String title;
    private String description;
    private Timestamp timestamp;
    private Color color;
    private String footerText;
    private String footerUrl;
    private String imageUrl;
    private String thumbnailUrl;
    private String authorName;
    private String authorUrl;
    private Set<EmbedField> fields;

    private Embed(String title, String description, Timestamp timestamp, Color color, String footerText, String footerUrl, String imageUrl, String thumbnailUrl, String authorName, String authorUrl, Set<EmbedField> fields) {
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.color = color;
        this.footerText = footerText;
        this.footerUrl = footerUrl;
        this.imageUrl = imageUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.authorName = authorName;
        this.authorUrl = authorUrl;
        this.fields = fields;
    }

    public static class Builder {
        private String title;
        private String description;
        private Timestamp timestamp;
        private Color color;
        private String footerText;
        private String footerUrl;
        private String imageUrl;
        private String thumbnailUrl;
        private String authorName;
        private String authorUrl;
        private Set<EmbedField> fields;

        public Builder() {
            this.title = "";
            this.description = "";
            this.timestamp = null;
            this.color = null;
            this.footerText = "";
            this.footerUrl = "";
            this.imageUrl = "";
            this.authorName = "";
            this.authorUrl = "";
            this.thumbnailUrl = "";
            this.fields = new HashSet<>();
        }

        public Builder addField(String name, String value) {
            this.fields.add(new EmbedField(name, value, false));
            return this;
        }

        public Builder addField(String name, int value) {
            this.fields.add(new EmbedField(name, String.valueOf(value), false));
            return this;
        }

        public Builder addField(String name, String value, boolean inline) {
            this.fields.add(new EmbedField(name, value, inline));
            return this;
        }

        public Builder addField(EmbedField field) {
            this.fields.add(field);
            return this;
        }

        public Builder setThumbnailUrl(String url) {
            this.thumbnailUrl = url;
            return this;
        }

        public Builder setAuthor(String name, String url) {
            this.authorName = name;
            this.authorUrl = url;
            return this;
        }

        public Builder setAuthor(String name) {
            this.authorName = name;
            return this;
        }

        public Builder setImageUrl(String url) {
            this.imageUrl = url;
            return this;
        }

        public Builder setFooter(String text, String url) {
            this.footerText = text;
            this.footerUrl = url;
            return this;
        }

        public Builder setFooter(String text) {
            this.footerText = text;
            return this;
        }

        public Builder setColor(Color color) {
            this.color = color;
            return this;
        }

        public Builder setTimestampNow() {
            this.timestamp = new Timestamp(System.currentTimeMillis());
            return this;
        }

        public Builder setTimestamp(Timestamp timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public final Embed build() {
            return new Embed(title, description, timestamp, color, footerText, footerUrl, imageUrl, thumbnailUrl, authorName, authorUrl, fields);
        }
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Color getColor() {
        return color;
    }

    public String getFooterText() {
        return footerText;
    }

    public String getFooterUrl() {
        return footerUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public ImmutableSet<EmbedField> getFields() {
        return ImmutableSet.copyOf(fields);
    }

    public final JsonNode toJson() {
        ObjectNode o = Constants.MAPPER.createObjectNode();

        if(!title.isEmpty()) {
            o.put("title", title);
        }

        if(!description.isEmpty()) {
            o.put("description", description);
        }

        o.put("type", "rich");

        if(timestamp != null) {
            o.put("timestamp", timestamp.toString());
        }

        if(color != null) {
            o.put("color", (0xFFFFFF & color.getRGB()));
        }

        ObjectNode footer = null;
        if(!footerText.isEmpty()) {
            footer = Constants.MAPPER.createObjectNode();
            footer.put("text", footerText);
        }

        if(!footerUrl.isEmpty()) {
            if(footer != null) {
                footer.put("icon_url", footerUrl);
            }
        }

        if(footer != null) {
            o.set("footer", footer);
        }

        if(!imageUrl.isEmpty()) {
            ObjectNode img = Constants.MAPPER.createObjectNode();
            img.put("url", imageUrl);

            o.set("image", img);
        }

        if(!thumbnailUrl.isEmpty()) {
            ObjectNode thumb = Constants.MAPPER.createObjectNode();
            thumb.put("url", thumbnailUrl);

            o.set("thumbnail", thumb);
        }

        ObjectNode author = null;
        if(!authorName.isEmpty()) {
            author = Constants.MAPPER.createObjectNode();
            author.put("name", authorName);
        }

        if(!authorUrl.isEmpty()) {
            if(author != null) {
                author.put("url", authorUrl);
            }
        }

        if(author != null) {
            o.set("author", author);
        }

        if(!fields.isEmpty()) {
            ArrayNode arr = Constants.MAPPER.createArrayNode();

            for(EmbedField field: fields) {
                ObjectNode el = Constants.MAPPER.createObjectNode();
                el.put("name", field.getName());
                el.put("value", field.getValue());
                el.put("inline", field.isInline());

                arr.add(el);
            }

            o.set("fields", arr);
        }

        return o;
    }

    public static Embed fromJson(JsonNode json) {
        String title = "";
        if(json.get("title") != null) {
            if(!json.get("title").isNull()) {
                title = json.get("title").asText();
            }
        }

        String desc = "";
        if(json.get("description") != null) {
            if(!json.get("description").isNull()) {
                desc = json.get("description").asText();
            }
        }

        Timestamp timestamp = null;
        if(json.get("timestamp") != null) {
            if(!json.get("timestamp").isNull()) {
                String str = json.get("timestamp").asText();
                str = StringUtils.replace(str, "+00:00", "");
                str = StringUtils.replace(str, "T", " ");

                timestamp = Timestamp.valueOf(str);
            }
        }

        Color color = null;
        if(json.get("color") != null) {
            if(!json.get("color").isNull()) {
                color = Color.decode(String.valueOf(json.get("color").asInt()));
            }
        }

        String footerText = "";
        String footerUrl = "";

        if(json.get("footer") != null) {
            if(!json.get("footer").isNull()) {
                JsonNode o = json.get("footer");

                if(o.get("text") != null) {
                    if(!o.get("text").isNull()) {
                        footerText = o.get("text").asText();
                    }
                }

                if(o.get("icon_url") != null) {
                    if(!o.get("icon_url").isNull()) {
                        footerUrl = o.get("icon_url").asText();
                    }
                }
            }
        }

        String imageUrl = "";
        if(json.get("image") != null) {
            if(!json.get("image").isNull()) {
                JsonNode o = json.get("image");
                imageUrl = o.get("url").asText();
            }
        }

        String thumbnailUrl = "";
        if(json.get("thumbnail") != null) {
            if(!json.get("thumbnail").isNull()) {
                JsonNode o = json.get("thumbnail");
                thumbnailUrl = o.get("url").asText();
            }
        }

        String authorName = "";
        String authorUrl = "";
        if(json.get("author") != null) {
            if(!json.get("author").isNull()) {
                JsonNode o = json.get("author");

                authorName = o.get("name").asText();

                if(o.get("url") != null) {
                    if(!o.get("url").isNull()) {
                        authorUrl = o.get("url").asText();
                    }
                }
            }
        }

        Set<EmbedField> fields = new HashSet<>();
        JsonNode arr = null;
        if(json.get("fields") != null) {
            if(!json.get("fields").isNull()) {
                arr = json.get("fields");
            }
        }

        if(arr != null) {
            for(JsonNode el: arr) {
                String name = el.get("name").asText();
                String value = el.get("value").asText();
                boolean inline = false;

                if(el.get("inline") != null) {
                    if(!el.get("inline").isNull()) {
                        inline = el.get("inline").asBoolean();
                    }
                }

                fields.add(new EmbedField(name, value, inline));
            }
        }
        System.gc();

        return new Embed(title, desc, timestamp, color, footerText, footerUrl, imageUrl, thumbnailUrl, authorName, authorUrl, fields);
    }
}
