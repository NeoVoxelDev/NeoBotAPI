package com.aurelian2842.nbapi.action.data;

import com.aurelian2842.nbapi.util.Sex;
import lombok.Data;

@Data
public class StrangerInfo {
    private final long userId;
    private final String nickname;
    private final Sex sex;
    private final int age;
}
