package net.trustgames.lobby.spawn;

import net.trustgames.core.Core;
import net.trustgames.core.managers.CooldownManager;
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

import java.util.Objects;

public class SpawnCommand implements CommandExecutor {

    private final Lobby lobby;

    public SpawnCommand(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        // methods and classes
        Core core = lobby.getCore();
        CooldownManager cooldownManager = core.cooldownManager;
        Spawn spawn = new Spawn(lobby);

        // configs
        FileConfiguration configCore = core.getConfig();
        FileConfiguration configLobby = lobby.getConfig();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(spawn.getSpawnFile());

        if (sender instanceof Player player) {

            // if the player has a cooldown on this command. Using CooldownManager from Core plugin.
            if (cooldownManager.commandCooldown(player, configLobby.getDouble("settings.cooldowns.spawn-command-cooldown"))){
                return true;
            }

            // gets the location from the spawn.yml
            Location location = config.getLocation("spawn.location");

            // if the location isn't null, teleport the player to the location and send him the teleport message
            if (location != null) {
                player.teleport(location);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(configLobby.getString("messages.spawn-teleport"))));
            } else {
                player.sendMessage(ChatColor.RED + "Spawn location isn't set!");
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(configCore.getString("messages.only-in-game-command"))));
        }
        return true;
    }
}
