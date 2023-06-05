package net.trustgames.lobby.protection.build;

import lombok.Getter;
import net.trustgames.core.gui.type.InventoryHandler;

import java.util.HashMap;
import java.util.UUID;

public class BuildProtectionAllowedPlayersMap extends HashMap<UUID, InventoryHandler> {

    private BuildProtectionAllowedPlayersMap(){}

    @Getter
    private static final BuildProtectionAllowedPlayersMap allowedPlayersMap = new BuildProtectionAllowedPlayersMap();
}
