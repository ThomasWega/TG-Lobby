package net.trustgames.lobby.protection.build;

import lombok.Getter;
import net.trustgames.core.gui.type.InventoryHandler;
import org.bukkit.GameMode;

import java.util.HashMap;
import java.util.UUID;

public class BuildProtectionAllowedPlayersMap extends HashMap<UUID, InventoryHandler> {

    private BuildProtectionAllowedPlayersMap(){}

    @Getter
    private static final BuildProtectionAllowedPlayersMap allowedMap = new BuildProtectionAllowedPlayersMap();


    public static class GameModesMap extends HashMap<UUID, GameMode> {
        private GameModesMap(){}

        @Getter
        private static final BuildProtectionAllowedPlayersMap.GameModesMap gamemodesMap = new GameModesMap();
    }
}
