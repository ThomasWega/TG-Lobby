package net.trustgames.lobby.spawn;

import net.trustgames.core.Core;
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

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class SpawnCommand implements CommandExecutor {

    private final Lobby lobby;

    public SpawnCommand(Lobby lobby) {
        this.lobby = lobby;
    }

    private final HashMap<UUID, Long> commandCooldown = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Core core = lobby.getCore();
        FileConfiguration configCore = core.getConfig();
        if (sender instanceof Player player) {
            Spawn spawn = new Spawn(lobby);

            // gets the location from the spawn.yml
            YamlConfiguration config = YamlConfiguration.loadConfiguration(spawn.getSpawnFile());
            FileConfiguration configMain = lobby.getConfig();
            Location location = config.getLocation("spawn.location");
            if (location != null) {
                player.teleport(location);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(configMain.getString("messages.spawn-teleport"))));
            } else {
                player.sendMessage(ChatColor.RED + "Spawn location isn't set!");
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(configCore.getString("messages.only-in-game-command"))));
        }
        return true;
    }
}
