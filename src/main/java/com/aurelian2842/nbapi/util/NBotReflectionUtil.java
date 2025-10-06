package com.aurelian2842.nbapi.util;

import com.aurelian2842.nbapi.event.NEvent;
import com.aurelian2842.nbapi.listener.NBotListener;
import com.aurelian2842.nbapi.listener.NBotEventHandler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class NBotReflectionUtil {
    public static List<NBotMethod> getEventMethods(NBotListener instance) {
        List<NBotMethod> set = new ArrayList<>();
        for (Method method : instance.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(NBotEventHandler.class)) {
                continue;
            }
            if (method.getParameterCount() > 1) {
                continue;
            } else if (method.getParameterCount() == 0) {
                set.add(new NBotMethod(instance, method, NEvent.class));
                method.setAccessible(true);
            } else {
                Class<?> parameterType = method.getParameterTypes()[0];
                if (!NEvent.class.isAssignableFrom(parameterType)) {
                    continue;
                }
                set.add(new NBotMethod(instance, method, (Class<? extends NEvent>) parameterType));
                method.setAccessible(true);
            }
        }
        return set;
    }
}
