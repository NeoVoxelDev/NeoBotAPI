package dev.neovoxel.nbapi.action.set;

import dev.neovoxel.nbapi.action.Action;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

import static dev.neovoxel.nbapi.util.NBotContentUtil.generateEcho;

@Data
@RequiredArgsConstructor
public class SetGroupWholeBan implements Action {
    private final long groupId;
    private final boolean enable;
    private String echo;

    @Override
    public JSONObject getData() {
        if (echo == null) {
            echo = generateEcho();
        }
        return new JSONObject()
                .put("action", "set_group_whole_ban")
                .put("params", new JSONObject()
                        .put("group_id", groupId)
                        .put("enable", enable))
                .put("echo", generateEcho());
    }
}
