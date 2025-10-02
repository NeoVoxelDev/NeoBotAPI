package com.aurelian2842.nbapi.util;

import com.aurelian2842.nbapi.event.NEvent;
import com.aurelian2842.nbapi.listener.NBotListener;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

@Data
public class NBotMethod {
    private final NBotListener instance;
    private final Method method;
    private final @Nullable Class<? extends NEvent> eventClass;
}
