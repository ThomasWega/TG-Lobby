package net.trustgames.lobby.spawn.commands;

import net.trustgames.core.Core;
import net.trustgames.core.config.CommandConfig;
import net.trustgames.core.config.CooldownConfig;
import net.trustgames.core.managers.CooldownManager;
import net.trustgames.lobby.Lobby;
import net.trustgames.lobby.config.LobbyPermissionConfig;
import net.trustgames.lobby.spawn.SpawnConfig;
import net.trustgames.lobby.spawn.SpawnHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * When a player executes /spawn, it teleports the player to the spawn location from spawn.yml (if any was set)
 */
public final class SpawnCommand implements CommandExecutor {

    private final Lobby lobby;

    public SpawnCommand(Lobby lobby) {
        this.lobby = lobby;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        Core core = lobby.getCore();
        CooldownManager cooldownManager = core.cooldownManager;
        SpawnHandler spawn = new SpawnHandler(lobby);

        YamlConfiguration config = YamlConfiguration.loadConfiguration(spawn.getSpawnFile());

        if (sender instanceof Player player) {
            // if the player has a cooldown on this command
            if (!player.hasPermission(LobbyPermissionConfig.STAFF.permission))
                if (cooldownManager.commandCooldown(player, CooldownConfig.MEDIUM.value)) {
                    return true;
                }
            Location location = config.getLocation("spawn.location");

            if (location != null) {
                Player target = player;
                if (args.length >= 1 && player.hasPermission(LobbyPermissionConfig.STAFF.permission)) {
                    for (String arg : args) {
                        target = Bukkit.getPlayer(arg);

                        if (target == null) {
                            player.sendMessage(CommandConfig.COMMAND_PLAYER_OFFLINE.addName(arg));
                            return true;
                        }

                        target.teleport(location);
                        target.sendMessage(SpawnConfig.SPAWN_TP.getMessage());
                        player.sendMessage(SpawnConfig.SPAWN_TP_OTHER.getMessage());
                    }
                } else {
                    target.teleport(location);
                    target.sendMessage(SpawnConfig.SPAWN_TP.getMessage());
                }
            } else {
                player.sendMessage(ChatColor.RED + "Spawn location isn't set!");
            }
        } else {
            sender.sendMessage(CommandConfig.COMMAND_PLAYER_ONLY.getText());
        }
        return true;
    }
}
