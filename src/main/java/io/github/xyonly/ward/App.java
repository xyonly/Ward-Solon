package io.github.xyonly.ward;

import io.github.xyonly.ward.plugin.SmHttpPluginNew;
import io.github.xyonly.ward.util.FileUtils;
import lombok.Getter;
import org.noear.solon.Solon;
import org.noear.solon.boot.smarthttp.integration.SmHttpPlugin;
import org.noear.solon.core.NvMap;
import org.noear.solon.core.PluginEntity;
import org.noear.solon.core.util.RunUtil;

import java.io.File;
import java.util.Optional;

/**
 * 应用启动类
 * 负责初始化系统配置，并控制应用的启动和重启逻辑
 *
 * @author 倚栏听风
 * @date 2025-03-28
 */
public class App {

    /**
     * 默认端口
     */
    public static final int INITIAL_PORT = 4000;

    /**
     * 是否为首次启动
     */
    @Getter
    public static boolean isFirstLaunch;

    public static void main(String[] args) {
        File setupFile = FileUtils.getSetUpFile();
        isFirstLaunch = !setupFile.exists();
        NvMap argx = NvMap.from(args);

        if (isFirstLaunch) {
            argx.put("server.port", String.valueOf(INITIAL_PORT));
        } else {
            argx.put("server.port", FileUtils.getValueFromIni("port"));
        }

        Solon.start(App.class, argx, app -> {
            // 排除原有的 HTTP 插件，使用新的 HTTP 插件
            app.pluginExclude(SmHttpPlugin.class);
            app.pluginAdd(0, new SmHttpPluginNew());
        });
    }

    /**
     * 重启应用
     */
    public static void restart() {
        isFirstLaunch = false;
        String port = FileUtils.getValueFromIni("port");
        RunUtil.delay(() -> restartServer(port), 100);
    }

    /**
     * 执行服务器重启
     *
     * @param port 新的端口
     */
    private static void restartServer(String port) {
        Optional<PluginEntity> pluginOptional = Solon.cfg().plugs().stream()
                .filter(p -> p.getPlugin() instanceof SmHttpPluginNew)
                .findFirst();

        pluginOptional.ifPresent(plugin -> {
            // 停止插件
            plugin.stop();
            // 重新设置端口
            Solon.cfg().setProperty("server.http.port", port);
            // 启动插件
            plugin.start(Solon.context());
        });
    }


}
