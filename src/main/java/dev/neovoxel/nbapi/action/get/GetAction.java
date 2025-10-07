package dev.neovoxel.nbapi.action.get;

import dev.neovoxel.nbapi.action.Action;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public interface GetAction<T> extends Action {
    @Nullable
    T parse(JSONObject jsonObject);
}
