package dev.neovoxel.nbapi.discord;

import dev.neovoxel.nbapi.discord.client.DiscordGatewayClient;

import java.net.URI;

/** Public entry point for Discord-specific clients. */
public final class DiscordClients {
    private DiscordClients() {
    }

    public static DiscordGatewayClient gateway(String botToken) {
        return new DiscordGatewayClient(botToken);
    }

    public static DiscordGatewayClient gateway(String botToken, int intents) {
        return new DiscordGatewayClient(botToken, intents);
    }

    public static DiscordGatewayClient gateway(String botToken, int intents, URI gateway, URI apiBase) {
        return new DiscordGatewayClient(gateway, apiBase, botToken, intents);
    }
}
