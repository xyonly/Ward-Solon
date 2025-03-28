package io.github.xyonly.ward.controller;


import io.github.xyonly.ward.Ward;
import io.github.xyonly.ward.exception.ApplicationNotConfiguredException;
import io.github.xyonly.ward.service.IndexService;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.handle.ModelAndView;

import java.io.IOException;
import java.util.Map;

/**
 * IndexController displays index page of Ward application
 *
 * @author Rudolf Barbu
 * @version 1.0.2
 */
@Controller
@Mapping(value = "/")
public class IndexController {
    /**
     * Autowired IndexService object
     * Used for getting index page template
     */
    @Inject
    private IndexService indexService;


    @Get
    @Mapping
    public ModelAndView getIndex() throws IOException, ApplicationNotConfiguredException {
        ModelAndView modelAndView = new ModelAndView();
        if (Ward.isFirstLaunch()) {
            return modelAndView.view("setup.html");
        }
        Map<String, Object> map = indexService.getIndex();
        modelAndView.putAll(map);
        return modelAndView.view("index.html");
    }
}