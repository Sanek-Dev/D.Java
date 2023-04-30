/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.channel;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.awt.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
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
            this.timestamp = Timestamp.from(OffsetDateTime.now().toInstant());
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

        public Embed build() {
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

    public Set<EmbedField> getFields() {
        return fields;
    }

    public JsonObject toJson() {
        JsonObject o = new JsonObject();

        if(!title.isEmpty()) {
            o.addProperty("title", title);
        }

        if(!description.isEmpty()) {
            o.addProperty("description", description);
        }

        o.addProperty("type", "rich");

        if(timestamp != null) {
            o.addProperty("timestamp", timestamp.toString());
        }

        if(color != null) {
            o.addProperty("color", (0xFFFFFF & color.getRGB()));
        }

        JsonObject footer = null;
        if(!footerText.isEmpty()) {
            footer = new JsonObject();
            footer.addProperty("text", footerText);
        }

        if(!footerUrl.isEmpty()) {
            if(footer != null) {
                footer.addProperty("icon_url", footerUrl);
            }
        }

        if(footer != null) {
            o.add("footer", footer);
        }

        if(!imageUrl.isEmpty()) {
            JsonObject img = new JsonObject();
            img.addProperty("url", imageUrl);

            o.add("image", img);
        }

        if(!thumbnailUrl.isEmpty()) {
            JsonObject thumb = new JsonObject();
            thumb.addProperty("url", thumbnailUrl);

            o.add("thumbnail", thumb);
        }

        JsonObject author = null;
        if(!authorName.isEmpty()) {
            author = new JsonObject();
            author.addProperty("name", authorName);
        }

        if(!authorUrl.isEmpty()) {
            if(author != null) {
                author.addProperty("url", authorUrl);
            }
        }

        if(author != null) {
            o.add("author", author);
        }

        if(!fields.isEmpty()) {
            JsonArray arr = new JsonArray();

            for(EmbedField field: fields) {
                JsonObject el = new JsonObject();
                el.addProperty("name", field.getName());
                el.addProperty("value", field.getValue());
                el.addProperty("inline", field.isInline());

                arr.add(el);
            }

            o.add("fields", arr);
        }

        return o;
    }

    public static Embed fromJson(JsonObject json) {
        String title = "";
        if(json.get("title") != null) {
            if(!json.get("title").isJsonNull()) {
                title = json.get("title").getAsString();
            }
        }

        String desc = "";
        if(json.get("description") != null) {
            if(!json.get("description").isJsonNull()) {
                desc = json.get("description").getAsString();
            }
        }

        Timestamp timestamp = null;
        if(json.get("timestamp") != null) {
            if(!json.get("timestamp").isJsonNull()) {
                String str = json.get("timestamp").getAsString();
                str = str.replace("+00:00", "");
                str = str.replace("T", " ");

                timestamp = Timestamp.valueOf(str);
            }
        }

        Color color = null;
        if(json.get("color") != null) {
            if(!json.get("color").isJsonNull()) {
                color = Color.decode(String.valueOf(json.get("color").getAsInt()));
            }
        }

        String footerText = "";
        String footerUrl = "";

        if(json.get("footer") != null) {
            if(!json.get("footer").isJsonNull()) {
                JsonObject o = json.get("footer").getAsJsonObject();

                if(o.get("text") != null) {
                    if(!o.get("text").isJsonNull()) {
                        footerText = o.get("text").getAsString();
                    }
                }

                if(o.get("icon_url") != null) {
                    if(!o.get("icon_url").isJsonNull()) {
                        footerUrl = o.get("icon_url").getAsString();
                    }
                }
            }
        }

        String imageUrl = "";
        if(json.get("image") != null) {
            if(!json.get("image").isJsonNull()) {
                JsonObject o = json.get("image").getAsJsonObject();
                imageUrl = o.get("url").getAsString();
            }
        }

        String thumbnailUrl = "";
        if(json.get("thumbnail") != null) {
            if(!json.get("thumbnail").isJsonNull()) {
                JsonObject o = json.get("thumbnail").getAsJsonObject();
                thumbnailUrl = o.get("url").getAsString();
            }
        }

        String authorName = "";
        String authorUrl = "";
        if(json.get("author") != null) {
            if(!json.get("author").isJsonNull()) {
                JsonObject o = json.get("author").getAsJsonObject();

                authorName = o.get("name").getAsString();

                if(o.get("url") != null) {
                    if(!o.get("url").isJsonNull()) {
                        authorUrl = o.get("url").getAsString();
                    }
                }
            }
        }

        Set<EmbedField> fields = new HashSet<>();
        JsonArray arr = null;
        if(json.get("fields") != null) {
            if(!json.get("fields").isJsonNull()) {
                arr = json.get("fields").getAsJsonArray();
            }
        }

        if(arr != null) {
            for(JsonElement el: arr) {
                JsonObject o = el.getAsJsonObject();

                String name = o.get("name").getAsString();
                String value = o.get("value").getAsString();
                boolean inline = false;

                if(o.get("inline") != null) {
                    if(!o.get("inline").isJsonNull()) {
                        inline = o.get("inline").getAsBoolean();
                    }
                }

                fields.add(new EmbedField(name, value, inline));
            }
        }

        return new Embed(title, desc, timestamp, color, footerText, footerUrl, imageUrl, thumbnailUrl, authorName, authorUrl, fields);
    }
}
