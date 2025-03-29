package io.github.xyonly.ward.service;


import io.github.xyonly.ward.component.UtilitiesComponent;
import io.github.xyonly.ward.exception.ApplicationNotConfiguredException;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * IndexService displays index page of Ward application
 *
 * @author Rudolf Barbu
 * @version 1.0.1
 */
@Component
public class IndexService {
    /**
     * Inject InfoService object
     * Used for getting machine information for html template
     */
    @Inject
    private InfoService infoService;

    /**
     * Inject UptimeService object
     * Used for getting uptime for html template
     */
    @Inject
    private UptimeService uptimeService;

    /**
     * Inject UtilitiesComponent object
     * Used for various utility functions
     */
    @Inject
    private UtilitiesComponent utilitiesComponent;

    /**
     * Gets project version information
     *
     * @return MavenDto with filled field
     */
    private String getVersion() {
        return Solon.cfg().get("ward.version", "Developer mode");
    }


    public Map<String, Object> getIndex() throws IOException, ApplicationNotConfiguredException {

        Map<String, Object> map = new HashMap<>();
        updateDefaultsInSetupFile();
        map.put("theme", utilitiesComponent.getFromIniFile("theme"));
        map.put("serverName", utilitiesComponent.getFromIniFile("serverName"));
        map.put("enableFog", utilitiesComponent.getFromIniFile("enableFog"));
        map.put("backgroundColor", utilitiesComponent.getFromIniFile("backgroundColor"));
        map.put("info", infoService.getInfo());
        map.put("uptime", uptimeService.getUptime());
        map.put("version", getVersion());
        return map;
    }

    public void updateDefaultsInSetupFile() throws IOException {
        if (utilitiesComponent.getFromIniFile("enableFog") == null) {
            utilitiesComponent.putInIniFile("enableFog", "true");
        }
        if (utilitiesComponent.getFromIniFile("backgroundColor") == null) {
            utilitiesComponent.putInIniFile("backgroundColor", "#303030");
        }
    }
}