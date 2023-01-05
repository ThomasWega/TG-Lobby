package net.trustgames.lobby.tablist;

import net.kyori.adventure.text.Component;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import net.trustgames.core.Core;
import net.trustgames.core.managers.LuckPermsManager;
import net.trustgames.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

public class TablistPlayerPrefix implements Listener {

    private final Lobby lobby;

    public TablistPlayerPrefix(Lobby lobby) {
        this.lobby = lobby;

        // register the user data change event
        EventBus eventBus = Core.getLuckPerms().getEventBus();
        eventBus.subscribe(lobby, UserDataRecalculateEvent.class, this::onUserDataRecalculate);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        initializePrefixes();
    }

    private void onUserDataRecalculate(UserDataRecalculateEvent event){
        initializePrefixes();
    }

    /* for each player on the server, get keys of name colors in config.yml
    and check if player has the permission to the highest one. If he does, break the
    loop and set rank as x (the rank). If he doesn't, continue until he has the permission
     */
    private void initializePrefixes(){

        Core core = lobby.getCore();
        FileConfiguration config = core.getConfig();

        for(Player player : Bukkit.getServer().getOnlinePlayers()){
            String rank = "default";
            for (String x : Objects.requireNonNull(config.getConfigurationSection("tablist.name-color")).getKeys(false)){
                if (player.hasPermission("lobby." + x)){
                    rank = x;
                    break;
                }
            }

            // get the name color
            String nameColor = config.getString("tablist.name-color." + rank);

            // get the prefix the player should have
            String prefix = LuckPermsManager.getUser(player).getCachedData().getMetaData().getPrefix();

            assert nameColor != null;
            // if the players primary group is default, don't set any prefix
            if (Objects.equals(LuckPermsManager.getPlayerPrimaryGroup(player), "default")){
                player.playerListName(Component.text(ChatColor.translateAlternateColorCodes('&', nameColor + player.getName())));
            // if the players primary group is anything else, set the prefix
            } else{
                player.playerListName(Component.text(ChatColor.translateAlternateColorCodes('&',  prefix + " " + nameColor + player.getName())));
            }
        }
    }

}
