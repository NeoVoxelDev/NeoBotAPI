package dev.neovoxel.nbapi.action.data;

import dev.neovoxel.nbapi.util.Role;
import dev.neovoxel.nbapi.util.Sex;
import lombok.Data;

@Data
public class GroupMemberInfo {
    private final long groupId;
    private final long userId;
    private final String nickname;
    private final String card;
    private final Sex sex;
    private final int age;
    private final String area;
    private final int joinTime;
    private final int lastSentTime;
    private final String level;
    private final Role role;
    private final boolean unfriendly;
    private final String title;
    private final int titleExpireTime;
    private final boolean cardChangeable;
}
