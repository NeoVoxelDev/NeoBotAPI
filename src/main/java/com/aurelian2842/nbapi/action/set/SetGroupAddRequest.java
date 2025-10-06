package com.aurelian2842.nbapi.action.set;

import com.aurelian2842.nbapi.action.Action;
import com.aurelian2842.nbapi.event.request.GroupRequestType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

import static com.aurelian2842.nbapi.util.NBotContentUtil.generateEcho;

@Data
@RequiredArgsConstructor
public class SetGroupAddRequest implements Action {
    private final String flag;
    private final GroupRequestType subType;
    private final boolean approve;
    private final String remark;
    private String echo;

    public SetGroupAddRequest(String flag, GroupRequestType type) {
        this(flag, type, true, "");
    }

    public SetGroupAddRequest(String flag, GroupRequestType type, boolean approve) {
        this(flag, type, approve, "");
    }

    @Override
    public JSONObject getData() {
        if (echo == null) {
            echo = generateEcho();
        }
        return new JSONObject()
                .put("action", "set_group_add_request")
                .put("params", new JSONObject()
                        .put("flag", flag)
                        .put("sub_type", subType.toString().toLowerCase())
                        .put("approve", approve)
                        .put("remark", remark))
                .put("echo", echo);
    }
}
