package net.trustgames.lobby.spawn.commands;

import net.trustgames.core.command.TrustCommand;
import net.trustgames.lobby.Lobby;
import net.trustgames.lobby.config.LobbyPermissionConfig;
import net.trustgames.lobby.spawn.SpawnConfig;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 * When a player with proper permission executes /setspawn, it saves his location to the spawn.yml file
 * The players are then teleported there on login
 */
public final class SetSpawnCommand extends TrustCommand {

    private final YamlConfiguration config;
    private final File configFile;

    public SetSpawnCommand(Lobby lobby) {
        super(LobbyPermissionConfig.ADMIN.permission);
        this.configFile = SpawnConfig.getSpawnFile(lobby);
        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = ((Player) sender);
        Location location = player.getLocation();
        config.set("spawn.location", location);
        try {
            config.save(configFile);
            player.sendMessage(ChatColor.GREEN + "Spawn location was set and saved in /plugins/Lobby/spawn.yml");
        } catch (IOException e) {
            player.sendMessage(ChatColor.RED + "ERROR: Couldn't save the spawn.yml file!");
            throw new RuntimeException(e);
        }
    }
}

