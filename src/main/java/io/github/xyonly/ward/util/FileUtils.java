package io.github.xyonly.ward.util;

import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;

import static io.github.xyonly.ward.common.Constants.SECTION_NAME;
import static io.github.xyonly.ward.common.Constants.SETUP_FILE_PATH;

/**
 * @author 倚栏听风
 * @date 2025-03-29 13:10
 */
public class FileUtils {

    public static File getSetUpFile() {
        return new File(SETUP_FILE_PATH);
    }

    /**
     * 从配置文件中获取指定键的值
     *
     * @param key 配置键
     * @return 配置值
     */
    public static String getValueFromIni(String key) {
        File setupFile = new File(SETUP_FILE_PATH);
        if (!setupFile.exists()) {
            return "";
        }

        try {
            Ini ini = new Ini(setupFile);
            String value = ini.get(SECTION_NAME, key);
            return value != null ? value : "";
        } catch (IOException e) {
            throw new RuntimeException("读取配置文件失败", e);
        }
    }
}
