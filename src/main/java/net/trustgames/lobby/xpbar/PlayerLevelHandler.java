package net.trustgames.lobby.xpbar;

import net.trustgames.core.Core;
import net.trustgames.core.cache.PlayerDataCache;
import net.trustgames.core.cache.UUIDCache;
import net.trustgames.core.player.data.config.PlayerDataType;
import net.trustgames.core.player.data.event.PlayerDataUpdateEvent;
import net.trustgames.core.utils.LevelUtils;
import net.trustgames.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Updates the player xp bar to match his progress towards the next level
 */
public final class PlayerLevelHandler implements Listener {

    private final Core core;

    public PlayerLevelHandler(Lobby lobby) {
        this.core = lobby.getCore();
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerJoin(PlayerJoinEvent event) {
        UUIDCache uuidCache = new UUIDCache(core, event.getPlayer().getName());
        uuidCache.get(uuid -> {
            if (uuid != null)
                sync(uuid);
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onDataUpdate(PlayerDataUpdateEvent event) {
        UUID uuid = event.getUuid();
        sync(uuid);
    }

    private void sync(@NotNull UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null && player.isOnline()) {
            PlayerDataCache dataCache = new PlayerDataCache(core, uuid, PlayerDataType.XP);
            dataCache.get(xp -> {
                assert xp != null; // xp never null
                int xpInt = Integer.parseInt(xp);
                int level = LevelUtils.getLevelByXp(xpInt);
                float levelProgress = LevelUtils.getProgress(xpInt);
                player.setExp(levelProgress);
                player.setLevel(level);
            });
        }
    }
}
