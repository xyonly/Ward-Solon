package io.github.xyonly.ward.service;

import io.github.xyonly.ward.component.UtilitiesComponent;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * ErrorService displays error pages of Ward application
 *
 * @author Rudolf Barbu
 * @version 1.0.1
 */
@Component
public class ErrorService {
    /**
     * Inject UtilitiesComponent object
     * Used for various utility functions
     */
    @Inject
    private UtilitiesComponent utilitiesComponent;


    public Map<String, Object> getError() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("theme", utilitiesComponent.getFromIniFile("theme"));
        return map;
    }
}