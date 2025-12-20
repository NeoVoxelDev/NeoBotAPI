package dev.neovoxel.nbapi.action.set;

import dev.neovoxel.nbapi.action.Action;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

import static dev.neovoxel.nbapi.util.NBotContentUtil.generateEcho;

@Data
@RequiredArgsConstructor
public class DeleteMessage implements Action {
    private final long messageId;
    private String echo;

    @Override
    public JSONObject getData() {
        if (echo == null) {
            echo = generateEcho();
        }
        return new JSONObject()
                .put("action", "delete_msg")
                .put("params", new JSONObject()
                        .put("message_id", messageId)
                )
                .put("echo", echo);
    }
}
