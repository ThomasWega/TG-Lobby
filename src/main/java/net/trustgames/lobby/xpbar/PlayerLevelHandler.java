package net.trustgames.lobby.xpbar;

import net.trustgames.core.Core;
import net.trustgames.core.cache.EntityCache;
import net.trustgames.core.config.database.player_data.PlayerDataType;
import net.trustgames.core.config.database.player_data.PlayerDataUpdate;
import net.trustgames.core.player.data.PlayerData;
import net.trustgames.core.player.data.additional.level.PlayerLevel;
import net.trustgames.lobby.Lobby;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

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
        UUID uuid = EntityCache.getUUID(player);
        PlayerData playerData = new PlayerData(core, uuid, PlayerDataType.XP);
        PlayerLevel playerLevel = new PlayerLevel(core, uuid);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) cancel();

                playerData.getData(xp -> playerLevel.getLevel(level -> {
                    float levelProgress = playerLevel.getProgress(xp);
                    player.setExp(levelProgress);
                    player.setLevel(level);
                }));
            }
        }.runTaskTimer(lobby, 4, PlayerDataUpdate.INTERVAL.getTicks());
    }
}
