package net.trustgames.lobby.tablist;

import net.kyori.adventure.text.Component;
import net.trustgames.lobby.Lobby;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TablistHeaderFooter implements Listener {

    private final Lobby lobby;

    public TablistHeaderFooter(Lobby lobby) {
        this.lobby = lobby;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        FileConfiguration config = lobby.getConfig();

        player.sendPlayerListHeader(Component.text(ChatColor.translateAlternateColorCodes('&', String.join("\n", config.getStringList("tablist.header")))));
        player.sendPlayerListFooter(Component.text(ChatColor.translateAlternateColorCodes('&', String.join("\n", config.getStringList("tablist.footer")))));
    }
}
