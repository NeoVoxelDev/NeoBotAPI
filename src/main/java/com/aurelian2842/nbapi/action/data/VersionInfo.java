package com.aurelian2842.nbapi.action.data;

import lombok.Data;

@Data
public class VersionInfo {
    private final String appName;
    private final String appVersion;
    private final String protocolVersion;
}
