package net.trustgames.lobby.xpbar;

import net.trustgames.core.Core;
import net.trustgames.core.cache.PlayerDataCache;
import net.trustgames.core.cache.UUIDCache;
import net.trustgames.core.config.player_data.PlayerDataType;
import net.trustgames.core.config.player_data.PlayerDataUpdate;
import net.trustgames.core.utils.LevelUtils;
import net.trustgames.lobby.Lobby;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public final class PlayerLevelHandler implements Listener {

    private final Lobby lobby;
    private final Core core;

    public PlayerLevelHandler(Lobby lobby) {
        this.lobby = lobby;
        this.core = lobby.getCore();
    }

    /**
     * Updates the player xp bar to match his progress towards the next level
     */
    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        UUIDCache uuidCache = new UUIDCache(core, playerName);
        uuidCache.get(uuid -> new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) cancel();

                assert uuid != null; // uuid never null
                PlayerDataCache dataCache = new PlayerDataCache(core, uuid, PlayerDataType.XP);
                dataCache.get(xp -> {
                    int xpInt = Integer.parseInt(xp);
                    int level = LevelUtils.getLevelByXp(xpInt);
                    float levelProgress = LevelUtils.getProgress(xpInt);
                    player.setExp(levelProgress);
                    player.setLevel(level);
                });
            }
        }.runTaskTimer(lobby, 4, PlayerDataUpdate.INTERVAL.getTicks()));
    }
}
