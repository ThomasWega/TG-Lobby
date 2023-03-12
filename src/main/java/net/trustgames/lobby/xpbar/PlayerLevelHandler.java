package net.trustgames.lobby.xpbar;

import net.trustgames.core.Core;
import net.trustgames.core.cache.UUIDCache;
import net.trustgames.core.config.player_data.PlayerDataType;
import net.trustgames.core.config.player_data.PlayerDataUpdate;
import net.trustgames.core.player.data.PlayerData;
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
    private final UUIDCache uuidCache;

    public PlayerLevelHandler(Lobby lobby) {
        this.lobby = lobby;
        this.core = lobby.getCore();
        this.uuidCache = core.getUuidCache();
    }

    /**
     * Updates the player xp bar to match his progress towards the next level
     */
    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        uuidCache.get(player.getName(), uuid -> {
            PlayerData playerData = new PlayerData(core, uuid, PlayerDataType.XP);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!player.isOnline()) cancel();
                    playerData.getData(xp -> {
                        int level = LevelUtils.getLevelByXp(xp);
                        float levelProgress = LevelUtils.getProgress(xp);
                        player.setExp(levelProgress);
                        player.setLevel(level);
                    });
                }
            }.runTaskTimer(lobby, 4, PlayerDataUpdate.INTERVAL.getTicks());
        });
    }
}
