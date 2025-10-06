package com.aurelian2842.nbapi.action.get;

import com.aurelian2842.nbapi.action.data.StrangerInfo;
import com.aurelian2842.nbapi.util.Sex;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

import java.util.Objects;

import static com.aurelian2842.nbapi.util.NBotContentUtil.generateEcho;

@Data
@RequiredArgsConstructor
public class GetStrangerInfo implements GetAction<StrangerInfo> {
    private final long userId;
    private final boolean noCache;
    private String echo;

    public GetStrangerInfo(long userId) {
        this.userId = userId;
        this.noCache = false;
    }

    @Override
    public StrangerInfo parse(JSONObject jsonObject) {
        if (jsonObject.getString("status").equals("failed")) {
            return null;
        }
        jsonObject = jsonObject.getJSONObject("data");
        return new StrangerInfo(
                jsonObject.getLong("user_id"),
                jsonObject.getString("nickname"),
                Sex.from(jsonObject.getString("sex")),
                jsonObject.getInt("age")
        );
    }

    @Override
    public JSONObject getData() {
        if (echo == null) {
            echo = generateEcho();
        }
        return new JSONObject()
                .put("action", "get_stranger_info")
                .put("params", new JSONObject()
                        .put("user_id", userId)
                        .put("no_cache", noCache)
                )
                .put("echo", echo);
    }
}
