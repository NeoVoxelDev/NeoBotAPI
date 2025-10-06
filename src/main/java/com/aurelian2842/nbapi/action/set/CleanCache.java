package com.aurelian2842.nbapi.action.set;

import com.aurelian2842.nbapi.action.Action;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

import static com.aurelian2842.nbapi.util.NBotContentUtil.generateEcho;

@Data
@RequiredArgsConstructor
public class CleanCache implements Action {
    private String echo;

    @Override
    public JSONObject getData() {
        if (echo == null) {
            echo = generateEcho();
        }
        return new JSONObject()
                .put("action", "clean_cache")
                .put("params", new JSONObject())
                .put("echo", echo);
    }
}
