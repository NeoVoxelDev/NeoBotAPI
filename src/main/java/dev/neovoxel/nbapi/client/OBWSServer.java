package dev.neovoxel.nbapi.client;

import dev.neovoxel.nbapi.action.Action;
import dev.neovoxel.nbapi.action.get.GetAction;
import dev.neovoxel.nbapi.event.NEvent;
import dev.neovoxel.nbapi.listener.NBotListener;
import dev.neovoxel.nbapi.util.NBotContentUtil;
import dev.neovoxel.nbapi.util.NBotMethod;
import dev.neovoxel.nbapi.util.NBotReflectionUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class OBWSServer extends WebSocketServer implements NBotClient {
    private static final Logger logger = LoggerFactory.getLogger(OBWSServer.class);

    private final List<NBotMethod> methods = new ArrayList<>();

    private final Map<GetAction<?>, Consumer<?>> consumerMap = new HashMap<>();

    private String accessToken;

    public OBWSServer(String address, int port) {
        super(new InetSocketAddress(address, port));
    }

    public OBWSServer(String address, int port, @Nullable String accessToken) {
        super(new InetSocketAddress(address, port));
        this.accessToken = accessToken;
    }

    @Override
    public void connect() {
        try {
            super.start();
        } catch (Exception e) {
            logger.error("Failed to start the websocket server", e);
        }
    }

    @Override
    public void disconnect() {
        try {
            super.stop();
        } catch (Exception e) {
            logger.error("Failed to stop the websocket server", e);
        }
    }

    @Override
    public void reconnect() {
        try {
            super.stop();
            super.start();
        } catch (Exception e) {
            logger.error("Failed to restart the websocket server", e);
        }
    }

    @Override
    public void addListener(NBotListener listener) {
        methods.addAll(NBotReflectionUtil.getEventMethods(listener));
    }

    @Override
    public void removeListener(NBotListener listener) {
        methods.removeAll(NBotReflectionUtil.getEventMethods(listener));
    }

    @Override
    public boolean hasListener(NBotListener listener) {
        return methods.stream().map(NBotMethod::getEventClass).collect(Collectors.toList()).contains(listener.getClass());
    }

    @Override
    public void action(Action action) {
        broadcast(action.getData().toString());
    }

    @Override
    public <T> void action(GetAction<T> action, Consumer<T> consumer) {
        broadcast(action.getData().toString());
        consumerMap.put(action, consumer);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        String token = clientHandshake.getFieldValue("Authorization").substring("Bearer ".length());
        if (this.accessToken == null || !Objects.equals(token, this.accessToken)) {
            logger.warn("A websocket client ({}) has connected with an invalid token, the program will close it.", webSocket.getLocalSocketAddress());
            webSocket.close(4001);
        }
        logger.info("A websocket client ({}) has connected", webSocket.getLocalSocketAddress());
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        logger.info("A websocket client ({}) has disconnected", webSocket.getLocalSocketAddress());
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        NEvent event = NBotContentUtil.onebot11Parse(s);
        if (event != null) {
            for (NBotMethod method : methods) {
                try {
                    if (method.getEventClass() == null) {
                        method.getMethod().invoke(method.getInstance());
                        continue;
                    }
                    if (method.getEventClass().isAssignableFrom(event.getClass())) {
                        method.getMethod().invoke(method.getInstance(), event);
                    }
                } catch (Exception e) {
                    logger.error("Failed to invoke listener class method", e);
                }
            }
        }
        JSONObject jsonObject = new JSONObject(s);
        if (jsonObject.has("echo")) {
            for (GetAction<?> getAction : consumerMap.keySet()) {
                String echo = getAction.getData().getString("echo");
                if (echo.equals(jsonObject.getString("echo"))) {
                    Consumer<Object> consumer = (Consumer<Object>) consumerMap.get(getAction);
                    consumer.accept(getAction.parse(jsonObject));
                }
            }
        }
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        logger.error("There's a error occurred in WebsocketServer", e);
    }

    @Override
    public boolean isConnected() {
        return !super.getConnections().isEmpty();
    }

    @Override
    public void onStart() {
        logger.info("The websocket server has started on {}", super.getAddress());
    }
}
