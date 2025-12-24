package dev.neovoxel.nbapi.client;

import dev.neovoxel.nbapi.action.Action;
import dev.neovoxel.nbapi.action.get.GetAction;
import dev.neovoxel.nbapi.event.NEvent;
import dev.neovoxel.nbapi.listener.NBotListener;
import dev.neovoxel.nbapi.util.NBotContentUtil;
import dev.neovoxel.nbapi.util.NBotMethod;
import dev.neovoxel.nbapi.util.NBotReflectionUtil;
import lombok.Getter;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@Getter
public class OBWSServer implements NBotClient {
    private static final Logger logger = LoggerFactory.getLogger(OBWSServer.class);

    private final List<NBotMethod> methods = new CopyOnWriteArrayList<>();
    private final Map<GetAction<?>, Consumer<?>> consumerMap = new ConcurrentHashMap<>();

    private final String address;
    private final int port;
    private final String accessToken;

    private InternalServer internalServer;

    public OBWSServer(String address, int port) {
        this(address, port, null);
    }

    public OBWSServer(String address, int port, @Nullable String accessToken) {
        this.address = address;
        this.port = port;
        this.accessToken = accessToken;
    }

    @Override
    public synchronized void connect() {
        if (internalServer != null) {
            logger.warn("Server is already initialized. Use reconnect() instead.");
            return;
        }
        createNewServerAndStart();
    }

    @Override
    public synchronized void disconnect() {
        if (internalServer != null) {
            try {
                internalServer.stop(1000);
                internalServer = null;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Failed to stop the websocket server", e);
            }
        }
    }

    @Override
    public synchronized void reconnect() {
        disconnect();
        try {
            Thread.sleep(200);
        } catch (InterruptedException ignored) {}
        createNewServerAndStart();
    }

    private void createNewServerAndStart() {
        this.internalServer = new InternalServer(new InetSocketAddress(address, port));
        this.internalServer.setReuseAddr(true);
        this.internalServer.start();
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
        return methods.stream().anyMatch(m -> m.getEventClass() == listener.getClass());
    }

    @Override
    public void action(Action action) {
        if (internalServer != null) {
            internalServer.broadcast(action.getData().toString());
        }
    }

    @Override
    public <T> void action(GetAction<T> action, Consumer<T> consumer) {
        if (internalServer != null) {
            internalServer.broadcast(action.getData().toString());
            consumerMap.put(action, consumer);
        }
    }

    @Override
    public boolean isConnected() {
        return internalServer != null && !internalServer.getConnections().isEmpty();
    }

    private class InternalServer extends WebSocketServer {
        public InternalServer(InetSocketAddress address) {
            super(address);
        }

        @Override
        public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
            String token = clientHandshake.getFieldValue("Authorization").substring("Bearer ".length());
            if (accessToken == null || !Objects.equals(token, accessToken)) {
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
                String receivedEcho = jsonObject.getString("echo");
                consumerMap.entrySet().removeIf(entry -> {
                    String targetEcho = entry.getKey().getData().optString("echo");
                    if (receivedEcho.equals(targetEcho)) {
                        ((Consumer<Object>) entry.getValue()).accept(entry.getKey().parse(jsonObject));
                        return true;
                    }
                    return false;
                });
            }
        }

        @Override
        public void onError(WebSocket webSocket, Exception e) {
            logger.error("There's a error occurred in WebsocketServer", e);
        }

        @Override
        public void onStart() {
            logger.info("The websocket server has started on {}", getAddress());
        }
    }
}