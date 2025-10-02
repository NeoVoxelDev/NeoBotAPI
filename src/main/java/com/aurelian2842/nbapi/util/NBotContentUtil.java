package com.aurelian2842.nbapi.util;

import com.aurelian2842.nbapi.event.NEvent;
import com.aurelian2842.nbapi.event.NUndefinedEvent;
import com.aurelian2842.nbapi.event.Sex;
import com.aurelian2842.nbapi.event.message.GroupMessageType;
import com.aurelian2842.nbapi.event.message.NGroupMessageEvent;
import com.aurelian2842.nbapi.event.message.NPrivateMessageEvent;
import com.aurelian2842.nbapi.event.message.PrivateMessageType;
import com.aurelian2842.nbapi.event.meta.NHeartbeatEvent;
import com.aurelian2842.nbapi.event.meta.NLifecycleEvent;
import com.aurelian2842.nbapi.event.notice.*;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class NBotContentUtil {
    private static final Logger logger = LoggerFactory.getLogger(NBotContentUtil.class);

    public static NEvent onebot11Parse(String content) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(content);
        } catch (JSONException e) {
            logger.error("Failed to parse content with OneBot 11 parser", e);
            return null;
        }
        long time = jsonObject.getLong("time");
        long selfId = jsonObject.getLong("self_id");
        String postType = jsonObject.getString("post_type");
        if (postType.equalsIgnoreCase("message")) {
            String messageType = jsonObject.getString("message_type");
            int messageId = jsonObject.getInt("message_id");
            long senderId = jsonObject.getLong("user_id");
            JSONArray message = jsonObject.getJSONArray("message");
            String rawMessage = jsonObject.getString("raw_message");
            if (messageType.equalsIgnoreCase("group")) {
                GroupMessageType type = GroupMessageType.from(jsonObject.getString("sub_type"));
                long groupId = jsonObject.getLong("group_id");
                if (type == GroupMessageType.ANONYMOUS) {
                    JSONObject anonymous = jsonObject.getJSONObject("anonymous");
                    long anonymousId = anonymous.getLong("id");
                    String anonymousName = anonymous.getString("name");
                    return new NGroupMessageEvent(time, selfId, messageId, senderId, message, rawMessage, type, groupId, anonymousId, anonymousName);
                } else {
                    return new NGroupMessageEvent(time, selfId, messageId, senderId, message, rawMessage, type, groupId, -1L, null);
                }
            } else if (messageType.equalsIgnoreCase("private")) {
                PrivateMessageType type = PrivateMessageType.from(jsonObject.getString("sub_type"));
                return new NPrivateMessageEvent(time, selfId, messageId, senderId, message, rawMessage, type);
            }
        } else if (postType.equalsIgnoreCase("meta_event")) {
            String metaEventType = jsonObject.getString("meta_event_type");
            if (metaEventType.equalsIgnoreCase("heartbeat")) {
                long interval = jsonObject.getLong("interval");
                JSONObject status = jsonObject.getJSONObject("status");
                boolean good = status.getBoolean("good");
                boolean online = status.getBoolean("online");
                return new NHeartbeatEvent(time, selfId, good, online, interval);
            } else if (metaEventType.equalsIgnoreCase("lifecycle")) {
                String subType = jsonObject.getString("sub_type");
                return new NLifecycleEvent(time, selfId, Objects.equals(subType, "connect"));
            }
        } else if (postType.equalsIgnoreCase("notice")) {
            String noticeType = jsonObject.getString("notice_type");
            if (noticeType.equalsIgnoreCase("group_decrease")) {
                GroupDecreaseType subType = GroupDecreaseType.from(jsonObject.getString("sub_type"));
                long groupId = jsonObject.getLong("group_id");
                long operatorId = jsonObject.getLong("operator_id");
                long userId = jsonObject.getLong("user_id");
                return new NGroupDecreaseEvent(time, selfId, subType, groupId, operatorId, userId);
            } else if (noticeType.equalsIgnoreCase("group_increase")) {
                GroupIncreaseType subType = GroupIncreaseType.from(jsonObject.getString("sub_type"));
                long groupId = jsonObject.getLong("group_id");
                long operatorId = jsonObject.getLong("operator_id");
                long userId = jsonObject.getLong("user_id");
                return new NGroupIncreaseEvent(time, selfId, subType, groupId, operatorId, userId);
            } else if (noticeType.equalsIgnoreCase("friend_add")) {
                long userId = jsonObject.getLong("user_id");
                return new NFriendAddEvent(time, selfId, userId);
            } else if (noticeType.equalsIgnoreCase("notify")) {
                long groupId = jsonObject.getLong("group_id");
                long userId = jsonObject.getLong("user_id");
                long targetId = jsonObject.getLong("target_id");
                return new NPokeEvent(time, selfId, groupId, userId, targetId);
            }
        }
        return new NUndefinedEvent(time, selfId, jsonObject);
    }
}
