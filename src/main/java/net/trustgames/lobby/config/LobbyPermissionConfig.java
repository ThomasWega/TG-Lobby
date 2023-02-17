package net.trustgames.lobby.config;

public enum LobbyPermissionConfig {
    ADMIN("lobby.admin"),
    STAFF("lobby.staff"),
    TITAN("lobby.titan"),
    LORD("lobby.lord"),
    KNIGHT("lobby.knight"),
    PRIME("lobby.prime"),
    DEFAULT("lobby.default");

    public final String permission;

    LobbyPermissionConfig(String permission) {
        this.permission = permission;
    }
}
