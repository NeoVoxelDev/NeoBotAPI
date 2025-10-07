package dev.neovoxel.nbapi.action.set;

import dev.neovoxel.nbapi.action.Action;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

import static dev.neovoxel.nbapi.util.NBotContentUtil.generateEcho;

@Data
@RequiredArgsConstructor
public class SetGroupCard implements Action {
    private final long groupId;
    private final long userId;
    private final String card;
    private String echo;

    @Override
    public JSONObject getData() {
        if (echo == null) {
            echo = generateEcho();
        }
        return new JSONObject()
                .put("action", "set_group_card")
                .put("params", new JSONObject()
                        .put("group_id", groupId)
                        .put("user_id", userId)
                        .put("card", card))
                .put("echo", echo);
    }
}
