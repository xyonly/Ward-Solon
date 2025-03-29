package io.github.xyonly.ward.controller;


import io.github.xyonly.ward.Ward;
import io.github.xyonly.ward.service.ErrorService;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;

import java.io.IOException;
import java.util.Map;

/**
 * ErrorController displays error pages of Ward application
 *
 * @author Rudolf Barbu
 * @version 1.0.2
 */
@Controller
@Mapping(value = "/error")
public class ErrorController {
    /**
     * Inject ErrorService object
     * Used to determine error page
     */
    @Inject
    private ErrorService errorService;

    /**
     * Get request to display error page, which corresponds status code
     *
     * @return String name of html template
     */
    @Get
    @Mapping
    public ModelAndView getError() throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        if (Ward.isFirstLaunch()) {
            return modelAndView.view("setup.html");
        }
        Map<String, Object> map = errorService.getError();
        modelAndView.putAll(map);
        return modelAndView.view("error/404.html");
    }

}