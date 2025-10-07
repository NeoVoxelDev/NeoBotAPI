package dev.neovoxel.nbapi.event.request;

public enum RequestType {
    GROUP,
    FRIEND;

    public static RequestType from(String s) {
        switch (s.toLowerCase()) {
            case "group":
                return GROUP;
            case "friend":
                return FRIEND;
            default:
                return null;
        }
    }
}
