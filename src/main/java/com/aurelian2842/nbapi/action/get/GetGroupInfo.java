package com.aurelian2842.nbapi.action.get;

import com.aurelian2842.nbapi.action.data.GroupInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.Objects;

import static com.aurelian2842.nbapi.util.NBotContentUtil.generateEcho;

@Data
@RequiredArgsConstructor
public class GetGroupInfo implements GetAction<GroupInfo> {
    private final long groupId;
    private final boolean noCache;
    private String echo;

    public GetGroupInfo(long groupId) {
        this.groupId = groupId;
        this.noCache = false;
    }

    @Override
    public GroupInfo parse(JSONObject jsonObject) {
        if (jsonObject.getString("status").equals("failed")) {
            return null;
        }
        jsonObject = jsonObject.getJSONObject("data");
        return new GroupInfo(
                jsonObject.getLong("group_id"),
                jsonObject.getString("group_name"),
                jsonObject.getInt("member_count"),
                jsonObject.getInt("max_member_count")
        );
    }

    @Override
    public JSONObject getData() {
        if (echo == null) {
            echo = generateEcho();
        }
        return new JSONObject()
                .put("action", "get_group_info")
                .put("params", new JSONObject()
                        .put("group_id", groupId)
                        .put("no_cache", noCache)
                )
                .put("echo", echo);
    }
}
