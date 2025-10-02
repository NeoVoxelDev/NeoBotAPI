package com.aurelian2842.nbapi.event.meta;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class NLifecycleEvent extends NMetaEvent {
    private final boolean connected;

    public NLifecycleEvent(long time, long selfId, boolean connected) {
        super(time, selfId, MetaEventType.LIFECYCLE);
        this.connected = connected;
    }
}
