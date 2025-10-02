package com.aurelian2842.nbapi.event.notice;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class NPokeEvent extends NNoticeEvent {
    private final long groupId;
    private final long userId;
    private final long targetId;

    public NPokeEvent(long time, long selfId, long groupId, long userId, long targetId) {
        super(time, selfId, NoticeType.POKE);
        this.groupId = groupId;
        this.userId = userId;
        this.targetId = targetId;
    }
}
