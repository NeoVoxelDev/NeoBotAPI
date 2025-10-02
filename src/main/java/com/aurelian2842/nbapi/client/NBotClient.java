package com.aurelian2842.nbapi.client;

import com.aurelian2842.nbapi.listener.NBotListener;

public interface NBotClient {
    void connect();

    void disconnect();

    void reconnect();

    void addListener(NBotListener listener);

    void removeListener(NBotListener listener);

    boolean hasListener(NBotListener listener);
}
