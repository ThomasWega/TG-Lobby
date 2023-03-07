package net.trustgames.lobby.hotbar;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.getInventory().clear();

        HotbarItems.addItems(player);
        HotbarItems.hidePlayersItem();
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        HotbarItems.hidePlayersItem();
    }

    @EventHandler
    private void itemClickEvent(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void itemDragEvent(InventoryDragEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void itemDropEvent(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }
}

