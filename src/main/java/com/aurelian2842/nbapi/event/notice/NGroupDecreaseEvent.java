package com.aurelian2842.nbapi.event.notice;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class NGroupDecreaseEvent extends NNoticeEvent {
    private final GroupDecreaseType subType;
    private final long groupId;
    private final long operatorId;
    private final long userId;

    public NGroupDecreaseEvent(long time, long selfId, GroupDecreaseType subType, long groupId, long operatorId, long userId) {
        super(time, selfId, NoticeType.GROUP_DECREASE);
        this.subType = subType;
        this.groupId = groupId;
        this.operatorId = operatorId;
        this.userId = userId;
    }
}
