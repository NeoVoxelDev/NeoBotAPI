package com.aurelian2842.nbapi.client;

import com.aurelian2842.nbapi.event.NEvent;
import com.aurelian2842.nbapi.listener.NBotListener;
import com.aurelian2842.nbapi.util.NBotContentUtil;
import com.aurelian2842.nbapi.util.NBotMapUtil;
import com.aurelian2842.nbapi.util.NBotMethod;
import com.aurelian2842.nbapi.util.NBotReflectionUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;


@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class OBWSClient extends WebSocketClient implements NBotClient {

    private static final Logger logger = LoggerFactory.getLogger(OBWSClient.class);

    private final Set<NBotMethod> methods = new HashSet<>();

    private final String address;
    private final int port;
    private final @Nullable String accessToken;

    private boolean isConnected = false;

    public OBWSClient(String address, int port) throws URISyntaxException {
        super(new URI("ws://" + address + ":" + port));
        this.address = address;
        this.port = port;
        this.accessToken = null;
    }

    public OBWSClient(String address, int port, @Nullable String accessToken) throws URISyntaxException {
        super(new URI("ws://" + address + ":" + port), NBotMapUtil.of("Authorization", "Bearer " + accessToken));
        this.address = address;
        this.port = port;
        this.accessToken = accessToken;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        isConnected = true;
    }

    @Override
    public void onMessage(String s) {
        NEvent event = NBotContentUtil.onebot11Parse(s);
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

    @Override
    public void onClose(int i, String s, boolean b) {
        isConnected = false;
    }

    @Override
    public void onError(Exception e) {
        isConnected = false;
    }

    @Override
    public void disconnect() {
        super.close();
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
        return false;
    }
}
