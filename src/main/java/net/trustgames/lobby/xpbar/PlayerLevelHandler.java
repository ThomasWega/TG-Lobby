package net.trustgames.lobby.xpbar;

import net.trustgames.toolkit.Toolkit;
import net.trustgames.toolkit.cache.PlayerDataCache;
import net.trustgames.toolkit.cache.UUIDCache;
import net.trustgames.toolkit.database.player.data.config.PlayerDataType;
import net.trustgames.toolkit.managers.rabbit.RabbitManager;
import net.trustgames.toolkit.managers.rabbit.extras.RabbitQueues;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static net.trustgames.toolkit.utils.LevelUtils.getLevelByXp;
import static net.trustgames.toolkit.utils.LevelUtils.getProgress;


/**
 * Updates the player xp bar to match his progress towards the next level
 */
public final class PlayerLevelHandler implements Listener {

    private final Toolkit toolkit;

    @Nullable
    private final RabbitManager rabbitManager;

    public PlayerLevelHandler(Toolkit toolkit) {
        this.toolkit = toolkit;
        this.rabbitManager = toolkit.getRabbitManager();
        onPlayerDataUpdate();
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerJoin(PlayerJoinEvent event) {
        UUIDCache uuidCache = new UUIDCache(toolkit, event.getPlayer().getName());
        uuidCache.get(uuid -> {
            if (uuid != null)
                update(uuid);
        });
    }

    private void update(@NotNull UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null && player.isOnline()) {
            PlayerDataCache dataCache = new PlayerDataCache(toolkit, uuid, PlayerDataType.XP);
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
