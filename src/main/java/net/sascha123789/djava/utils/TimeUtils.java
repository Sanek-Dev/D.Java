/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.utils;

import java.sql.Timestamp;

public class TimeUtils {
    public static String format(Timestamp timestamp) {
        return "<t:" + timestamp.getTime() + ">";
    }

    public static String format(Timestamp timestamp, Style style) {
        return "<t:" + timestamp.getTime() + ":" + style.getCode() + ">";
    }

    public static enum Style {
        SHORT_TIME("t"), LONG_TIME("T"), SHORT_DATE("d"), LONG_DATE("D"), SHORT_DATE_TIME("f"), LONG_DATE_TIME("F"), RELATIVE_TIME("R");

        private String code;

        Style(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
}
