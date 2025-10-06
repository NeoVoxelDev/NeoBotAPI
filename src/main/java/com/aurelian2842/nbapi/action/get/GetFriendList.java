package com.aurelian2842.nbapi.action.get;

import com.aurelian2842.nbapi.action.data.FriendInfo;
import com.aurelian2842.nbapi.action.data.FriendList;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.aurelian2842.nbapi.util.NBotContentUtil.generateEcho;

@Data
@RequiredArgsConstructor
public class GetFriendList implements GetAction<FriendList> {
    private String echo;

    @Override
    public FriendList parse(JSONObject jsonObject) {
        if (jsonObject.getString("status").equals("failed")) {
            return null;
        }
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        FriendList list = new FriendList();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            FriendInfo friendInfo = new FriendInfo(
                    object.getLong("user_id"),
                    object.getString("nickname"),
                    object.getString("remark")
            );
            list.add(friendInfo);
        }
        return list;
    }

    @Override
    public JSONObject getData() {
        if (echo == null) {
            echo = generateEcho();
        }
        return new JSONObject()
                .put("action", "get_friend_list")
                .put("params", new JSONObject())
                .put("echo", echo);
    }
}
