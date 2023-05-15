package net.trustgames.lobby.hotbar;

import net.trustgames.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Events which set, change, update or remove the hotbar items.
 * Also handles the item move, drop, drag
 */
public final class HotbarHandler implements Listener {

    public HotbarHandler(Lobby lobby) {
        Bukkit.getServer().getPluginManager().registerEvents(this, lobby);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.getInventory().clear();

        HotbarItems.addItems(player);
        HotbarItems.hidePlayersItem();
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerQuit(PlayerQuitEvent event) {
        HotbarItems.hidePlayersItem();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void itemClickEvent(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void itemDragEvent(InventoryDragEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void itemDropEvent(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }
}

