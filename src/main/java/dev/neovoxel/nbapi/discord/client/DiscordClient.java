package dev.neovoxel.nbapi.discord.client;

import dev.neovoxel.nbapi.discord.data.DiscordMessage;
import dev.neovoxel.nbapi.listener.NBotListener;

import java.util.concurrent.CompletableFuture;

/**
 * Discord-specific bot client. Discord channels and snowflake IDs do not map
 * cleanly to OneBot groups, so they intentionally have a separate API.
 */
public interface DiscordClient {
    void connect();

    void disconnect();

    /** Permanently closes this client and its background resources. */
    void shutdown();

    void reconnect();

    boolean isConnected();

    void addListener(NBotListener listener);

    void removeListener(NBotListener listener);

    boolean hasListener(NBotListener listener);

    CompletableFuture<DiscordMessage> sendMessage(long channelId, String content);

    CompletableFuture<DiscordMessage> editMessage(long channelId, long messageId, String content);

    CompletableFuture<Void> deleteMessage(long channelId, long messageId);
}
