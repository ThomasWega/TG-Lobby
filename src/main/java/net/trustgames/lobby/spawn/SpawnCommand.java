package net.trustgames.lobby.spawn;

import net.trustgames.core.Core;
import net.trustgames.core.config.command.CommandConfig;
import net.trustgames.core.config.cooldown.CooldownConfig;
import net.trustgames.core.managers.CooldownManager;
import net.trustgames.lobby.Lobby;
import net.trustgames.lobby.config.command.LobbyCommandConfig;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 When a player executes /spawn, it teleports the player to the spawn location from spawn.yml (if any was set)
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

        YamlConfiguration config = YamlConfiguration.loadConfiguration(spawn.getSpawnFile());

        if (sender instanceof Player player) {

            // if the player has a cooldown on this command
            if (cooldownManager.commandCooldown(player, CooldownConfig.MEDIUM.getValue())){
                return true;
            }

            Location location = config.getLocation("spawn.location");

            if (location != null) {
                player.teleport(location);

                player.sendMessage(LobbyCommandConfig.SPAWN_TP.getMessage());
            } else {
                player.sendMessage(ChatColor.RED + "Spawn location isn't set!");
            }
        } else {
            sender.sendMessage(CommandConfig.COMMAND_ONLY_PLAYER.getText());
        }
        return true;
    }
}
