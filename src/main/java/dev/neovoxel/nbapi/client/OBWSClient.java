package dev.neovoxel.nbapi.client;

import dev.neovoxel.nbapi.action.Action;
import dev.neovoxel.nbapi.action.get.GetAction;
import dev.neovoxel.nbapi.event.NEvent;
import dev.neovoxel.nbapi.listener.NBotListener;
import dev.neovoxel.nbapi.util.NBotContentUtil;
import dev.neovoxel.nbapi.util.NBotMapUtil;
import dev.neovoxel.nbapi.util.NBotMethod;
import dev.neovoxel.nbapi.util.NBotReflectionUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Consumer;


@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class OBWSClient extends WebSocketClient implements NBotClient {

    private static final Logger logger = LoggerFactory.getLogger(OBWSClient.class);

    private final List<NBotMethod> methods = new ArrayList<>();

    private final Map<GetAction<?>, Consumer<?>> consumerMap = new HashMap<>();

    private boolean isConnected = false;

    public OBWSClient(String address, int port) throws URISyntaxException {
        super(new URI("ws://" + address + ":" + port));
    }

    public OBWSClient(String address, int port, @Nullable String accessToken) throws URISyntaxException {
        super(new URI("ws://" + address + ":" + port), NBotMapUtil.of("Authorization", "Bearer " + accessToken));
    }

    public OBWSClient(URI url, @Nullable String accessToken) {
        super(url, NBotMapUtil.of("Authorization", "Bearer " + accessToken));
    }

    public OBWSClient(URI url) {
        super(url);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        isConnected = true;
    }

    @Override
    public void onMessage(String s) {
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
    public void onClose(int i, String s, boolean b) {
        isConnected = false;
    }

    @Override
    public void onError(Exception e) {
        logger.error("There's a error occurred in WebsocketClient", e);
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

    @Override
    public void action(Action action) {
        send(action.getData().toString());
    }

    @Override
    public <T> void action(GetAction<T> action, Consumer<T> consumer) {
        send(action.getData().toString());
        consumerMap.put(action, consumer);
    }
}
