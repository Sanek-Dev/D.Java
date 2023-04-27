/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.utils;

import net.sascha123789.djava.api.enums.ImageType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;

public class ImageUtils {
    public static byte[] toByteArray(String url) {
        URL url1 = null;
        try {
            url1 = new URL(url);
        } catch(Exception e) {
            e.printStackTrace();
        }

        try(InputStream stream = url1.openStream()) {
            return IOUtils.toByteArray(stream);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toDataString(String url, ImageType type) {
        try {
            byte[] data = toByteArray(url);
            String encoded = Base64.getEncoder().encodeToString(data);

            return "data:image/" + (type == ImageType.GIF ? "gif" : (type == ImageType.PNG ? "png" : (type == ImageType.JPEG ? "jpeg" : "webp"))) + ";base64," + encoded;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toDataString(File file, ImageType type) {
        try {
            byte[] data = FileUtils.readFileToByteArray(file);
            String encoded = Base64.getEncoder().encodeToString(data);

            return "data:image/" + (type == ImageType.GIF ? "gif" : (type == ImageType.PNG ? "png" : (type == ImageType.JPEG ? "jpeg" : "webp"))) + ";base64," + encoded;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
