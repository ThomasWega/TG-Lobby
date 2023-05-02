package net.trustgames.lobby.spawn.commands;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.Command;
import cloud.commandframework.paper.PaperCommandManager;
import net.trustgames.lobby.Lobby;
import net.trustgames.lobby.spawn.SpawnConfig;
import net.trustgames.toolkit.config.PermissionConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class SetSpawnCommand {

    private final PaperCommandManager<CommandSender> commandManager;
    private final Lobby lobby;

    public SetSpawnCommand(Lobby lobby) {
        this.commandManager = lobby.getCommandManager();
        this.lobby = lobby;
        register();
    }

    private void register() {
        Command.Builder<CommandSender> setSpawnCommand = commandManager.commandBuilder(
                "setspawn",
                ArgumentDescription.of("ADD"));

        commandManager.command(setSpawnCommand
                .senderType(Player.class)
                .permission(PermissionConfig.STAFF.permission)
                .handler(context -> {
                    Player player = ((Player) context.getSender());
                    if (SpawnConfig.updateSpawnLocation(lobby, player.getLocation())) {
                        player.sendMessage(ChatColor.GREEN + "Spawn location was set at " +
                                "your location and saved in /plugins/Lobby/spawn.yml");
                    } else {
                        player.sendMessage(ChatColor.RED + "ERROR: Couldn't save the spawn.yml file!");
                    }
                })
        );
    }
}
