package io.github.xyonly.ward.component;

import io.github.xyonly.ward.Ward;
import io.github.xyonly.ward.common.Constants;
import org.ini4j.Ini;
import org.noear.solon.annotation.Component;

import java.io.File;
import java.io.IOException;

import static io.github.xyonly.ward.common.Constants.SECTION_NAME;

/**
 * @author 倚栏听风
 * @date 2025-03-28 16:10
 */
@Component
public class UtilitiesComponent {

    /**
     * Gets string data from ini file
     *
     * @param optionName option in section
     * @return String with parsed data
     * @throws IOException if file does not exist
     */
    @SuppressWarnings(value = "MismatchedQueryAndUpdateOfCollection")
    public String getFromIniFile(final String optionName) throws IOException {
        final File file = new File(Constants.SETUP_FILE_PATH);

        if (file.exists()) {
            final Ini ini = new Ini(file);
            return ini.get(SECTION_NAME, optionName, String.class);
        }

        return null;
    }

    /**
     * Sets string data to the ini file
     *
     * @param optionName option in section
     * @param value      value to put
     * @throws IOException if file does not exist
     */
    public void putInIniFile(final String optionName, final String value) throws IOException {
        final File file = new File(Constants.SETUP_FILE_PATH);

        if (file.exists()) {
            final Ini ini = new Ini(file);
            ini.put(SECTION_NAME, optionName, value);
            ini.store();
        } else {
            throw new IOException("The INI file does not exist");
        }
    }
}
