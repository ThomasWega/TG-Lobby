package net.trustgames.lobby.spawn;

import net.trustgames.core.Core;
import net.trustgames.core.managers.ColorManager;
import net.trustgames.lobby.Lobby;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

/**
 * When a player with proper permission executes /setspawn, it saves his location to the spawn.yml file
 * The players are then teleported there on login
 */
public class SetSpawnCommand implements CommandExecutor {

    private final Lobby lobby;

    public SetSpawnCommand(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        Core core = lobby.getCore();
        Spawn spawn = new Spawn(lobby);

        FileConfiguration configCore = core.getConfig();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(spawn.getSpawnFile());

        if (sender instanceof Player player) {
            if (player.hasPermission("lobby.admin")) {
                Location location = player.getLocation();
                config.set("spawn.location", location);
                try {
                    config.save(spawn.getSpawnFile());
                    player.sendMessage(ChatColor.GREEN + "Spawn location was set and saved in /plugins/Lobby/spawn.yml!");
                } catch (IOException e) {
                    player.sendMessage(ChatColor.RED + "ERROR: Couldn't save the spawn.yml file!");
                    throw new RuntimeException(e);
                }
            } else {
                String path = "messages.no-permission";
                player.sendMessage(ColorManager.translateColors(Objects.requireNonNull(configCore.getString(path),
                        "String on path " + path + " wasn't found in config!")));
            }
        } else {
            String path = "messages.only-in-game-command";
            sender.sendMessage(ColorManager.translateColors(Objects.requireNonNull(configCore.getString(path),
                    "String on path " + path + " wasn't found in config!")));
        }
        return true;
    }
}

