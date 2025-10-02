package com.aurelian2842.nbapi.event.message;

import com.aurelian2842.nbapi.event.Sex;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.json.JSONArray;
import org.json.JSONObject;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class NPrivateMessageEvent extends NMessageEvent {
    private final PrivateMessageType subType;

    public NPrivateMessageEvent(long time, long selfId, int messageId, long senderId, JSONArray message, String rawMessage, PrivateMessageType subType) {
        super(time, selfId, MessageType.PRIVATE, messageId, senderId, message, rawMessage);
        this.subType = subType;
    }
}
