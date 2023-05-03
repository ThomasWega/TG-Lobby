package net.trustgames.lobby.xpbar;

import net.trustgames.toolkit.Toolkit;
import net.trustgames.toolkit.cache.PlayerDataCache;
import net.trustgames.toolkit.database.player.data.config.PlayerDataType;
import net.trustgames.toolkit.database.player.data.event.PlayerDataUpdateEvent;
import net.trustgames.toolkit.database.player.data.event.PlayerDataUpdateEventManager;
import net.trustgames.toolkit.database.player.data.event.PlayerDataUpdateListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static net.trustgames.toolkit.utils.LevelUtils.getLevelByXp;
import static net.trustgames.toolkit.utils.LevelUtils.getProgress;


/**
 * Updates the player xp bar to match his progress towards the next level
 */
public final class PlayerLevelHandler implements PlayerDataUpdateListener, Listener {

    private final Toolkit toolkit;

    public PlayerLevelHandler(Toolkit toolkit) {
        this.toolkit = toolkit;
        PlayerDataUpdateEventManager.register(this);
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerJoin(PlayerJoinEvent event) {
        update(event.getPlayer().getUniqueId());
    }

    /**
     * Updates the Player's XP bar with his current xp and level
     *
     * @param uuid UUID of the player
     */
    private void update(@NotNull UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null && player.isOnline()) {
            PlayerDataCache dataCache = new PlayerDataCache(toolkit, uuid, PlayerDataType.XP);
            dataCache.get(xp -> {
                if (xp.isEmpty()) return;

                int xpInt = Integer.parseInt(xp.get());
                int level = getLevelByXp(xpInt);
                float levelProgress = getProgress(xpInt);
                player.setExp(levelProgress);
                player.setLevel(level);
            });
        }
    }

    @Override
    public void onPlayerDataUpdate(PlayerDataUpdateEvent event) {
        update(event.getUuid());
    }
}
