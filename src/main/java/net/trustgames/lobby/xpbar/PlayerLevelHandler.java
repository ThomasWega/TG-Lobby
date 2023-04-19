package net.trustgames.lobby.xpbar;

import net.trustgames.middleware.Middleware;
import net.trustgames.middleware.cache.PlayerDataCache;
import net.trustgames.middleware.cache.UUIDCache;
import net.trustgames.middleware.database.player.data.config.PlayerDataType;
import net.trustgames.middleware.managers.rabbit.RabbitManager;
import net.trustgames.middleware.managers.rabbit.extras.RabbitQueues;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static net.trustgames.middleware.utils.LevelUtils.getLevelByXp;
import static net.trustgames.middleware.utils.LevelUtils.getProgress;


/**
 * Updates the player xp bar to match his progress towards the next level
 */
public final class PlayerLevelHandler implements Listener {

    private final Middleware middleware;

    @Nullable
    private final RabbitManager rabbitManager;

    public PlayerLevelHandler(Middleware middleware) {
        this.middleware = middleware;
        this.rabbitManager = middleware.getRabbitManager();
        onPlayerDataUpdate();
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerJoin(PlayerJoinEvent event) {
        UUIDCache uuidCache = new UUIDCache(middleware, event.getPlayer().getName());
        uuidCache.get(uuid -> {
            if (uuid != null)
                update(uuid);
        });
    }

    private void update(@NotNull UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null && player.isOnline()) {
            PlayerDataCache dataCache = new PlayerDataCache(middleware, uuid, PlayerDataType.XP);
            dataCache.get(xp -> {
                assert xp != null; // xp never null
                int xpInt = Integer.parseInt(xp);
                int level = getLevelByXp(xpInt);
                float levelProgress = getProgress(xpInt);
                player.setExp(levelProgress);
                player.setLevel(level);
            });
        }
    }


    public void onPlayerDataUpdate() {
        if (rabbitManager == null) return;

        rabbitManager.onChannelInitialized(() ->
                rabbitManager.onDelivery(
                        RabbitQueues.EVENT_PLAYER_DATA_UPDATE.name,
                        json -> {
                            UUID uuid = UUID.fromString(json.getString("uuid"));
                            update(uuid);
                        })
        );
    }
}
