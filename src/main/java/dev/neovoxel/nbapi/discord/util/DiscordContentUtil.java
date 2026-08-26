package dev.neovoxel.nbapi.discord.util;

import dev.neovoxel.nbapi.discord.data.DiscordMessage;
import dev.neovoxel.nbapi.discord.data.DiscordUser;
import dev.neovoxel.nbapi.discord.DiscordSnowflake;
import dev.neovoxel.nbapi.discord.event.DiscordEvent;
import dev.neovoxel.nbapi.discord.event.DiscordMessageDeleteEvent;
import dev.neovoxel.nbapi.discord.event.DiscordMessageEvent;
import dev.neovoxel.nbapi.discord.event.DiscordReadyEvent;
import org.json.JSONObject;

import java.time.Instant;
import java.time.format.DateTimeParseException;

public final class DiscordContentUtil {
    private DiscordContentUtil() {
    }

    public static DiscordEvent parseDispatch(String eventName, JSONObject data, long selfId) {
        long time = eventTime(data);
        if ("READY".equals(eventName)) {
            DiscordUser user = DiscordUser.from(data.getJSONObject("user"));
            return new DiscordReadyEvent(time, user.getId(), user, data.getString("session_id"), data);
        }
        if ("MESSAGE_CREATE".equals(eventName) || "MESSAGE_UPDATE".equals(eventName)) {
            return new DiscordMessageEvent(time, selfId, eventName, DiscordMessage.from(data), data);
        }
        if ("MESSAGE_DELETE".equals(eventName)) {
            Long guildId = data.has("guild_id") && !data.isNull("guild_id")
                    ? DiscordSnowflake.parse(data.getString("guild_id")) : null;
            return new DiscordMessageDeleteEvent(
                    time,
                    selfId,
                    DiscordSnowflake.parse(data.getString("id")),
                    DiscordSnowflake.parse(data.getString("channel_id")),
                    guildId,
                    data
            );
        }
        return new DiscordEvent(time, selfId, eventName, data);
    }

    private static long eventTime(JSONObject data) {
        String timestamp = data.has("timestamp") && !data.isNull("timestamp")
                ? data.getString("timestamp")
                : data.optString("edited_timestamp", null);
        if (timestamp != null) {
            try {
                return Instant.parse(timestamp).getEpochSecond();
            } catch (DateTimeParseException ignored) {
                // Some Discord events do not use the ISO instant form.
            }
        }
        return Instant.now().getEpochSecond();
    }
}
