package com.aurelian2842.nbapi.event.notice;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class NFriendAddEvent extends NNoticeEvent {
    private final long userId;

    public NFriendAddEvent(long time, long selfId, long userId) {
        super(time, selfId, NoticeType.FRIEND_ADD);
        this.userId = userId;
    }
}
