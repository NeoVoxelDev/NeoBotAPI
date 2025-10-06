package com.aurelian2842.nbapi.action.get;

import com.aurelian2842.nbapi.action.Action;
import lombok.Data;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public interface GetAction<T> extends Action {
    @Nullable
    T parse(JSONObject jsonObject);
}
