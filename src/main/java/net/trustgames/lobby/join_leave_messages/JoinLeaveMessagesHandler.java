package net.trustgames.lobby.join_leave_messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.trustgames.core.managers.item.ItemBuilder;
import net.trustgames.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class JoinLeaveMessagesHandler implements Listener {

    public JoinLeaveMessagesHandler(Lobby lobby) {
        Bukkit.getPluginManager().registerEvents(this, lobby);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void onPlayerJoin(PlayerJoinEvent event) {
        event.joinMessage(null);
        Bukkit.broadcast(JoinLeaveMessagesConfig.JOIN_MSG.formatMessage(event.getPlayer()));

        ItemBuilder builder = new ItemBuilder(Material.DIAMOND)
                .displayName(Component.text("lala", NamedTextColor.GREEN));
        event.getPlayer().getInventory().addItem(builder.build());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void onPlayerQuit(PlayerQuitEvent event) {
        event.quitMessage(null);
        Bukkit.broadcast(JoinLeaveMessagesConfig.LEAVE_MSG.formatMessage(event.getPlayer()));
    }
}
