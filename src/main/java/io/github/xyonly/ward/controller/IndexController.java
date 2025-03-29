package io.github.xyonly.ward.controller;


import io.github.xyonly.ward.Ward;
import io.github.xyonly.ward.exception.ApplicationNotConfiguredException;
import io.github.xyonly.ward.service.IndexService;
import io.github.xyonly.ward.util.FileUtils;
import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.Context;
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
     * Inject IndexService object
     * Used for getting index page template
     */
    @Inject
    private IndexService indexService;


    @Get
    @Mapping
    public ModelAndView getIndex(Context ctx) throws IOException, ApplicationNotConfiguredException {
        ModelAndView modelAndView = new ModelAndView();
        if (Ward.isFirstLaunch()) {
            return modelAndView.view("setup.html");
        }
        if (ctx.session("ward.password") == null) {
            return modelAndView.view("login.html");
        }
        Map<String, Object> map = indexService.getIndex();
        modelAndView.putAll(map);
        return modelAndView.view("index.html");
    }

    @Post
    @Mapping("login")
    public void login(String password, Context ctx)  {
        String pass = FileUtils.getValueFromIni("password");
        if (password.equals(pass)) {
            ctx.sessionSet("ward.password", password);
            ctx.redirect("/");
        } else {
            ctx.redirect("/login");
        }

    }
    @Get
    @Mapping("login")
    public ModelAndView loginView()  {
       return new ModelAndView("login.html");

    }
}