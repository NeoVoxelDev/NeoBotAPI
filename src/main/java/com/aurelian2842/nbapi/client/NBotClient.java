package com.aurelian2842.nbapi.client;

import com.aurelian2842.nbapi.action.Action;
import com.aurelian2842.nbapi.action.get.GetAction;
import com.aurelian2842.nbapi.listener.NBotListener;

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
