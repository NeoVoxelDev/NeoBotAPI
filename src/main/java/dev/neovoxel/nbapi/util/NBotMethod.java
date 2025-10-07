package dev.neovoxel.nbapi.util;

import dev.neovoxel.nbapi.event.NEvent;
import dev.neovoxel.nbapi.listener.NBotListener;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

@Data
public class NBotMethod {
    private final NBotListener instance;
    private final Method method;
    private final @Nullable Class<? extends NEvent> eventClass;
}
