package net.trustgames.lobby.settings;

import net.trustgames.core.managers.NPCManager;

public class LobbySettings {

    // prefixes
    public static final String PREFIX_SPAWN = "&#dbfca4Spawn | ";
    public static final String PREFIX_JOIN = "&#6b8ae8Join | ";
    public static final String PREFIX_LEAVE = "&#6b8ae8Leave | ";

    // messages
    public static final String SPAWN_TP = PREFIX_SPAWN + "&8You've been teleported to the spawn location.";

    // cooldowns
    public static final double COOLDOWN_SPAWN = 3d;

    // doublejump
    public static final double DOUBLE_JUMP_HOR = 0.8d;
    public static final double DOUBLE_JUMP_VER = 0.8d;
    public static final double DOUBLE_JUMP_HOR_RUN = 0.85d;
    public static final double DOUBLE_JUMP_VER_RUN = 0.6d;

    // join-leave
    public static final String JOIN_MSG = PREFIX_JOIN + "&e%1$s &f%2$s &7has joined the lobby!";
    public static final String LEAVE_MSG = PREFIX_LEAVE + "&e%1$s &f%2$s &7has left the lobby!";

}
