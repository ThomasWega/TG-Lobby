package net.trustgames.lobby.spawn;

import net.trustgames.lobby.Lobby;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SetSpawnCommand implements CommandExecutor {

        private final Lobby lobby;

        public SetSpawnCommand(Lobby lobby) {
                this.lobby = lobby;
        }

        /*
         When a player with proper permission executes /setspawn, it saves his location to the spawn.yml file
         The player are then teleported there on login
        */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("core.admin")) {
                Location location = player.getLocation();
                Spawn spawn = new Spawn(lobby);
                YamlConfiguration config = YamlConfiguration.loadConfiguration(spawn.getSpawnFile());
                config.set("spawn.location", location);
                try {
                    config.save(spawn.getSpawnFile());
                    player.sendMessage(ChatColor.GREEN + "Spawn location was set and saved in spawn.yml!");
                } catch (IOException e) {
                    player.sendMessage(ChatColor.RED + "Couldn't save the spawn.yml file!");
                    throw new RuntimeException(e);
                }
            } else {
                player.sendMessage(ChatColor.RED + "You don't have permission to perform this command!");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "This command can be used by players only!");
        }
        return true;
    }
}

