package com.aurelian2842.nbapi.event.request;

public enum GroupRequestType {
    ADD,
    INVITE;

    public static GroupRequestType from(String s) {
        switch (s.toLowerCase()) {
            case "add":
                return ADD;
            case "invite":
                return INVITE;
            default:
                return null;
        }
    }
}
