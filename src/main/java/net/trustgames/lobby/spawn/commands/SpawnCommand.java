package net.trustgames.lobby.spawn.commands;

import net.kyori.adventure.text.Component;
import net.trustgames.core.command.TrustCommand;
import net.trustgames.core.config.CommandConfig;
import net.trustgames.core.config.CooldownConfig;
import net.trustgames.core.managers.CooldownManager;
import net.trustgames.lobby.Lobby;
import net.trustgames.lobby.config.LobbyPermissionConfig;
import net.trustgames.lobby.spawn.SpawnConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * When a player executes /spawn, it teleports the player to the spawn location from spawn.yml (if any was set)
 */
public final class SpawnCommand extends TrustCommand {

    private final YamlConfiguration config;

    public SpawnCommand(Lobby lobby) {
        super(null);
        this.config = YamlConfiguration.loadConfiguration(SpawnConfig.getSpawnFile(lobby));
    }


    @Override
    public void execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = ((Player) sender);
        // if the player is not staff and has a cooldown on this command
        if (!player.hasPermission(LobbyPermissionConfig.STAFF.permission)) {
            if (CooldownManager.handle(player, CooldownConfig.MEDIUM.value)) {
                return;
            }
        }
        Location location = config.getLocation("spawn.location");

        if (location != null) {
            Player target = player;
            if (args.length >= 1 && player.hasPermission(LobbyPermissionConfig.STAFF.permission)) {
                for (String arg : args) {
                    target = Bukkit.getPlayer(arg);

                    if (target == null) {
                        player.sendMessage(CommandConfig.COMMAND_PLAYER_OFFLINE.addComponent(Component.text(arg)));
                        return;
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
    }
}
