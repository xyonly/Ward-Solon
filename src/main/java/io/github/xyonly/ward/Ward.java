package io.github.xyonly.ward;

import io.github.xyonly.ward.plugin.SmHttpPluginNew;
import lombok.Getter;
import org.ini4j.Ini;
import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.boot.smarthttp.integration.SmHttpPlugin;
import org.noear.solon.core.NvMap;
import org.noear.solon.core.PluginEntity;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.util.RunUtil;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static io.github.xyonly.ward.common.Constants.SECTION_NAME;


/**
 * @author 倚栏听风
 * @date 2025-03-28 15:56
 */
public class Ward {
    /**
     * Constant for determine settings file name
     */
    public static final String SETUP_FILE_PATH = "setup.ini";

    /**
     * Constant for determine initial application port
     */
    public static final int INITIAL_PORT = 4000;

    /**
     * Holder for determine first launch of application
     */
    @Getter
    public static boolean isFirstLaunch;

    /**
     * Holder for application context
     */
    private static SolonApp solonApp;


    public static void main(String[] args) throws IOException {
        File setupFile = new File(Ward.SETUP_FILE_PATH);
        isFirstLaunch = !setupFile.exists();
        NvMap argx = NvMap.from(args);
        if (isFirstLaunch) {
            argx.put("server.port", String.valueOf(Ward.INITIAL_PORT));
        } else {
            Ini ini = new Ini(setupFile);
            argx.put("server.port", String.valueOf(ini.get(SECTION_NAME, "port")));
        }
        solonApp = Solon.start(Ward.class, argx, app -> {
            //排除原来的 http 插件
            app.pluginExclude(SmHttpPlugin.class);
            //添加新的 http 插件
            app.pluginAdd(0, new SmHttpPluginNew());
        });
    }

    /**
     * Restarts application
     */
    public static void restart() throws IOException {
        isFirstLaunch = false;
        File setupFile = new File(Ward.SETUP_FILE_PATH);
        Ini ini = new Ini(setupFile);
        String port = String.valueOf(ini.get(SECTION_NAME, "port"));

        //前端重定向
        Context ctx = solonApp.context().getBean(Context.class);
        RunUtil.delay(() -> rebootDo(port), 50);//延后执行
        if (ctx != null) {
            ctx.contentType("text/html;charset=UTF-8");
            ctx.output("<html><head><script>");
            ctx.output("setTimeout(function() { window.location.href = 'http://localhost:" + port + "'; }, 500);");
            ctx.output("</script></head><body></body></html>");
        }
    }

    private static void rebootDo(String port) {
        Optional<PluginEntity> pluginOptional = Solon.cfg().plugs().stream()
                .filter(p -> p.getPlugin() instanceof SmHttpPluginNew)
                .findFirst();
        if (pluginOptional.isPresent()) {
            //停止
            pluginOptional.get().stop();
            //启动
            Solon.cfg().setProperty("server.http.port", port);
            pluginOptional.get().start(Solon.context());
        }
    }
}
