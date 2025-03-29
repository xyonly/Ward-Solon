package io.github.xyonly.ward.service;


import io.github.xyonly.ward.App;
import io.github.xyonly.ward.common.Constants;
import io.github.xyonly.ward.dao.ResponseDto;
import io.github.xyonly.ward.dao.SetupDto;
import io.github.xyonly.ward.exception.ApplicationAlreadyConfiguredException;
import org.ini4j.Ini;
import org.noear.solon.annotation.Component;

import java.io.File;
import java.io.IOException;

/**
 * SetupService manipulating setup data
 *
 * @author Rudolf Barbu
 * @version 1.0.2
 */
@Component
public class SetupService {


    /**
     * Puts new data in ini file
     *
     * @param file       ini file
     * @param optionName option in section
     * @throws IOException if file does not exists
     */
    private static void putInIniFile(final File file, final String optionName, final String value) throws IOException {
        Ini ini = new Ini(file);
        ini.put(Constants.SECTION_NAME, optionName, value);
        ini.store();
    }

    /**
     * Fills setup data in ini file
     *
     * @param setupDto user settings data
     * @return ResponseEntityWrapperAsset filled with ResponseDto
     * @throws IOException IoException if file is fot found, and cant be created
     */
    public ResponseDto postSetup(final SetupDto setupDto) throws IOException, ApplicationAlreadyConfiguredException {
        if (App.isFirstLaunch()) {
            File file = new File(Constants.SETUP_FILE_PATH);

            if (file.createNewFile()) {
                putInIniFile(file, "serverName", setupDto.getServerName());
                putInIniFile(file, "password", setupDto.getPassword());
                putInIniFile(file, "theme", setupDto.getTheme());
                putInIniFile(file, "port", setupDto.getPort());
                putInIniFile(file, "enableFog", setupDto.getEnableFog());
                putInIniFile(file, "backgroundColor", setupDto.getBackgroundColor());

                App.restart();
            } else {
                throw new IOException();
            }
        } else {
            throw new ApplicationAlreadyConfiguredException();
        }

        return new ResponseDto("Settings saved correctly");
    }

    @Deprecated
    public static ResponseDto envSetup() {
        if (App.isFirstLaunch()) {
            try {
                File file = new File(Constants.SETUP_FILE_PATH);
                if (file.exists()) {
                    file.delete();
                }
                if (file.createNewFile()) {
                    String servername = (System.getenv("WARD_NAME") != null) ? System.getenv("WARD_NAME") : "Ward";
                    String theme = (System.getenv("WARD_THEME") != null) ? System.getenv("WARD_THEME").toLowerCase() : "light";
                    String port = (System.getenv("WARD_PORT") != null) ? System.getenv("WARD_PORT") : "4000";
                    String enableFog = (System.getenv("WARD_FOG") != null) ? System.getenv("WARD_FOG") : "true";
                    String backgroundColor = (System.getenv("WARD_BACKGROUND") != null) ? System.getenv("WARD_BACKGROUND") : "default";

                    putInIniFile(file, "serverName", servername);
                    putInIniFile(file, "theme", theme);
                    putInIniFile(file, "port", port);
                    putInIniFile(file, "enableFog", enableFog);
                    putInIniFile(file, "backgroundColor", backgroundColor);

                    App.restart();
                } else {
                    throw new IOException();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new ResponseDto("Settings saved correctly");
    }
}