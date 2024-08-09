package com.infotec.ftp.client.utils;

import java.io.File;
import java.io.IOException;

public class Utils {
    public static void checkFileOrCreate(String path) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        if (path.isEmpty()) {
            throw new IllegalArgumentException("File path cannot be empty");
        }
        File file = new File(path);
        if (!file.exists()) {
            boolean ignored = file.createNewFile();
        }
    }

    public static String toSystemFilePath(String path) {
        return path.replace('\\', File.separatorChar);
    }
}
