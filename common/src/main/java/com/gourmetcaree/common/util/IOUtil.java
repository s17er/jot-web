package com.gourmetcaree.common.util;

import org.apache.log4j.Logger;

import java.io.Closeable;
import java.io.IOException;

public class IOUtil {

    private static final Logger logger = Logger.getLogger(IOUtil.class);

    public static void close(Closeable closeable) {
        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (IOException e) {
            logger.warn("クローズに失敗しました。", e);
        }
    }
}
