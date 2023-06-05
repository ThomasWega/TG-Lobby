package net.trustgames.lobby.spawn.commands;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.Command;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.bukkit.parsers.PlayerArgument;
import cloud.commandframework.paper.PaperCommandManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.trustgames.lobby.Lobby;
import net.trustgames.lobby.spawn.SpawnConfig;
import net.trustgames.lobby.spawn.SpawnLocation;
import net.trustgames.toolkit.config.PermissionConfig;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public final class SpawnCommand {
    private final Lobby lobby;
    private final PaperCommandManager<CommandSender> commandManager;

    public SpawnCommand(Lobby lobby) {
        this.lobby = lobby;
        this.commandManager = lobby.getCommandManager();

        Command.Builder<CommandSender> spawnCommand = commandManager.commandBuilder(
                "spawn",
                ArgumentDescription.of("ADD"));

        registerPersonalCommand(spawnCommand);
        registerTargetCommand(spawnCommand);
    }

    private void registerPersonalCommand(Command.Builder<CommandSender> spawnCommand) {
        commandManager.command(spawnCommand
                .senderType(Player.class)
                .handler(context -> {
                    Player player = ((Player) context.getSender());
                    Location location = SpawnLocation.getLocation(lobby);
                    if (location == null) {
                        player.sendMessage(Component.text("Spawn location not set!", NamedTextColor.RED));
                        return;
                    }
                    player.teleportAsync(location, PlayerTeleportEvent.TeleportCause.COMMAND)
                            .thenRun(() -> player.sendMessage(SpawnConfig.SPAWN_TP.getFormatted()));
                })
        );
    }

    private void registerTargetCommand(Command.Builder<CommandSender> spawnCommand) {

        // TARGET argument
        CommandArgument<CommandSender, Player> targetArg = PlayerArgument.of("target");

        commandManager.command(spawnCommand
                .permission(PermissionConfig.STAFF.getPermission())
                .argument(targetArg)
                .handler(context -> {
                    CommandSender sender = context.getSender();
                    Player target = context.get(targetArg);
                    Location location = SpawnLocation.getLocation(lobby);
                    if (location == null) {
                        sender.sendMessage(Component.text("Spawn location isn't set!", NamedTextColor.RED));
                        return;
                    }

                    target.teleportAsync(location, PlayerTeleportEvent.TeleportCause.COMMAND)
                            .thenRun(() -> {
                                target.sendMessage(SpawnConfig.SPAWN_TP.getFormatted());
                                // if the sender and target are the same person
                                if (!sender.getName().equals(target.getName())) {
                                    sender.sendMessage(SpawnConfig.SPAWN_TP_OTHER.getFormatted());
                                }
                            });
                })
        );
    }
}
