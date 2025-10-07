package dev.neovoxel.nbapi.client;

import dev.neovoxel.nbapi.action.Action;
import dev.neovoxel.nbapi.action.get.GetAction;
import dev.neovoxel.nbapi.listener.NBotListener;

import java.util.function.Consumer;

public interface NBotClient {
    void connect();

    void disconnect();

    void reconnect();

    void addListener(NBotListener listener);

    void removeListener(NBotListener listener);

    boolean hasListener(NBotListener listener);

    void action(Action action);

    <T> void action(GetAction<T> action, Consumer<T> consumer);
}
