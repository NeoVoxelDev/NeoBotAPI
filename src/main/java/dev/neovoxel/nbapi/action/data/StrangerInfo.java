package dev.neovoxel.nbapi.action.data;

import dev.neovoxel.nbapi.util.Sex;
import lombok.Data;

@Data
public class StrangerInfo {
    private final long userId;
    private final String nickname;
    private final Sex sex;
    private final int age;
}
