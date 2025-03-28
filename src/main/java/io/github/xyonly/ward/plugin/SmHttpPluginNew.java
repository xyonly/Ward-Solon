package io.github.xyonly.ward.plugin;

import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.boot.ServerConstants;
import org.noear.solon.boot.ServerProps;
import org.noear.solon.boot.prop.impl.HttpServerProps;
import org.noear.solon.boot.prop.impl.WebSocketServerProps;
import org.noear.solon.boot.smarthttp.SmHttpServerComb;
import org.noear.solon.boot.smarthttp.http.MultipartUtil;
import org.noear.solon.core.*;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.util.LogUtil;

public final class SmHttpPluginNew implements Plugin {
    private static Signal _signal;

    public static Signal signal() {
        return _signal;
    }


    public static String solon_boot_ver() {
        return "smart http 2.5/" + Solon.version();
    }

    private SmHttpServerComb _server;

    @Override
    public void start(AppContext context) {
        if (context.app().enableHttp() == false) {
            return;
        }

        context.lifecycle(ServerConstants.SIGNAL_LIFECYCLE_INDEX, () -> {
            start0(context);
        });
    }

    private void start0(AppContext context) throws Throwable {
        //初始化属性
        ServerProps.init();
        MultipartUtil.init();

        //todo: 这里是定制的重点，每次会重新实例化（旧的会缓存）
        HttpServerProps props = new HttpServerProps();
        final String _host = props.getHost();
        final int _port = props.getPort();
        final String _name = props.getName();

        long time_start = System.currentTimeMillis();


        _server = new SmHttpServerComb();
        _server.enableWebSocket(context.app().enableWebSocket());
        _server.setCoreThreads(props.getCoreThreads());

        if (props.isIoBound()) {
            //如果是io密集型的，加二段线程池
            _server.setExecutor(props.newWorkExecutor("smarthttp-"));
        }

        _server.setHandler(context.app()::tryHandle);

        //尝试事件扩展
        EventBus.publish(_server);
        _server.start(_host, _port);


        final String _wrapHost = props.getWrapHost();
        final int _wrapPort = props.getWrapPort();
        _signal = new SignalSim(_name, _wrapHost, _wrapPort, "http", SignalType.HTTP);
        context.app().signalAdd(_signal);

        long time_end = System.currentTimeMillis();

        String connectorInfo = "solon.connector:main: smarthttp: Started ServerConnector@{HTTP/1.1,[http/1.1]";
        if (context.app().enableWebSocket()) {
            //有名字定义时，添加信号注册
            WebSocketServerProps wsProps = WebSocketServerProps.getInstance();
            if (Utils.isNotEmpty(wsProps.getName())) {
                SignalSim wsSignal = new SignalSim(wsProps.getName(), _wrapHost, _wrapPort, "ws", SignalType.WEBSOCKET);
                context.app().signalAdd(wsSignal);
            }

            String wsServerUrl = props.buildWsServerUrl(_server.isSecure());
            LogUtil.global().info(connectorInfo + "[WebSocket]}{" + wsServerUrl + "}");
        }

        String httpServerUrl = props.buildHttpServerUrl(_server.isSecure());
        LogUtil.global().info(connectorInfo + "}{" + httpServerUrl + "}");
        LogUtil.global().info("Server:main: smarthttp: Started (" + solon_boot_ver() + ") @" + (time_end - time_start) + "ms");
    }

    @Override
    public void stop() throws Throwable {
        if (_server != null) {
            _server.stop();
            _server = null;

            LogUtil.global().info("Server:main: smarthttp: Has Stopped (" + solon_boot_ver() + ")");
        }
    }
}