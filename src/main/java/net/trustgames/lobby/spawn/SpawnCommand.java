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

public class SpawnCommand implements CommandExecutor {

    private final Lobby lobby;

    public SpawnCommand(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            Spawn spawn = new Spawn(lobby);

            // gets the location from the spawn.yml
            YamlConfiguration config = YamlConfiguration.loadConfiguration(spawn.getSpawnFile());
            Location location = config.getLocation("spawn.location");
            if (location != null) {
                player.teleport(location);
            } else {
                player.sendMessage(ChatColor.RED + "Spawn location isn't set!");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "This command can be used by in-game players only!");
        }
        return true;
    }
}
