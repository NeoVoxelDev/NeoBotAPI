package dev.neovoxel.nbapi.action.get;

import dev.neovoxel.nbapi.action.data.StatusInfo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import static dev.neovoxel.nbapi.util.NBotContentUtil.generateEcho;

@Data
@RequiredArgsConstructor
public class GetStatus implements GetAction<StatusInfo> {
    private String echo;

    @Override
    public @Nullable StatusInfo parse(JSONObject jsonObject) {
        if (jsonObject.getString("status").equals("failed")) {
            return null;
        }
        jsonObject = jsonObject.getJSONObject("data");
        return new StatusInfo(
                jsonObject.getBoolean("online"),
                jsonObject.getBoolean("good")
        );
    }

    @Override
    public JSONObject getData() {
        if (echo == null) {
            echo = generateEcho();
        }
        return new JSONObject()
                .put("action", "get_status")
                .put("params", new JSONObject())
                .put("echo", echo);
    }
}
