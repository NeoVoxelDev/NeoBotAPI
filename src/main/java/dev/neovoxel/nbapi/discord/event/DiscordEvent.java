package dev.neovoxel.nbapi.discord.event;

import dev.neovoxel.nbapi.event.NEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.json.JSONObject;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class DiscordEvent extends NEvent {
    private final String eventName;
    private final JSONObject rawData;

    public DiscordEvent(long time, long selfId, String eventName, JSONObject rawData) {
        super(time, selfId);
        this.eventName = eventName;
        this.rawData = rawData;
    }
}
