package net.trustgames.lobby.spawn;

import net.trustgames.core.Core;
import net.trustgames.core.managers.ColorManager;
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

/**
 When a player executes /spawn, it teleports the player to the spawn location from spawn.yml (if any is was set)
 */
public class SpawnCommand implements CommandExecutor {

    private final Lobby lobby;

    public SpawnCommand(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        Core core = lobby.getCore();
        CooldownManager cooldownManager = core.cooldownManager;
        Spawn spawn = new Spawn(lobby);

        FileConfiguration configCore = core.getConfig();
        FileConfiguration configLobby = lobby.getConfig();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(spawn.getSpawnFile());

        if (sender instanceof Player player) {

            // if the player has a cooldown on this command
            if (cooldownManager.commandCooldown(player, configLobby.getDouble("settings.cooldowns.spawn-command-cooldown"))){
                return true;
            }

            Location location = config.getLocation("spawn.location");

            if (location != null) {
                player.teleport(location);

                String path = "messages.spawn-teleport";
                player.sendMessage(ColorManager.color(Objects.requireNonNull(
                        configLobby.getString(path), "String on path " + path + " wasn't found in config!")));
            } else {
                player.sendMessage(ChatColor.RED + "Spawn location isn't set!");
            }
        } else {
            String path = "messages.only-in-game-command";
            sender.sendMessage(ColorManager.color(Objects.requireNonNull(
                    configCore.getString(path), "String on path " + path + " wasn't found in config!")));
        }
        return true;
    }
}
