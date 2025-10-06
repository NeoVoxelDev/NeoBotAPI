package com.aurelian2842.nbapi.action.data;

import lombok.Data;

@Data
public class GroupInfo {
    private final long groupId;
    private final String groupName;
    private final int memberCount;
    private final int maxMemberCount;
}
