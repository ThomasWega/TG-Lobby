package net.trustgames.lobby.protection.build;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.Command;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.flags.CommandFlag;
import cloud.commandframework.bukkit.parsers.PlayerArgument;
import cloud.commandframework.paper.PaperCommandManager;
import net.kyori.adventure.text.Component;
import net.trustgames.toolkit.config.PermissionConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

import static net.trustgames.lobby.protection.build.BuildProtectionHandler.allowedPlayers;

public class BuildProtectionCommand {

    private final PaperCommandManager<CommandSender> commandManager;

    public BuildProtectionCommand(PaperCommandManager<CommandSender> commandManager) {
        this.commandManager = commandManager;

        // MAIN COMMAND
        Command.Builder<CommandSender> buildCommand = commandManager.commandBuilder(
                        "build",
                        ArgumentDescription.of(
                                "Allows the sender or target to freely place and destroy blocks"))
                .permission(PermissionConfig.STAFF.getPermission());

        personalCommand(buildCommand);
        targetsCommand(buildCommand);
    }

    // command /build
    private void personalCommand(Command.Builder<CommandSender> buildCommand) {
        commandManager.command(buildCommand
                .senderType(Player.class)
                .handler(context -> {
                    Player player = ((Player) context.getSender());
                    UUID uuid = player.getUniqueId();
                    if (allowedPlayers.contains(uuid)) {
                        allowedPlayers.remove(uuid);
                        player.sendMessage(BuildProtectionConfig.SENDER_OFF.getFormatted());
                    } else {
                        allowedPlayers.add(uuid);
                        player.sendMessage(BuildProtectionConfig.SENDER_ON.getFormatted());
                    }
                })
        );
    }

    // command /build <target> <Optional --silent>
    private void targetsCommand(Command.Builder<CommandSender> buildCommand) {

        // TARGET argument
        CommandArgument<CommandSender, Player> targetArg = PlayerArgument.of("target");

        // SILENT flag
        CommandFlag<Void> silentFlag = CommandFlag.builder("silent")
                .withDescription(ArgumentDescription.of(
                        "Whether the target player should be notified")
                ).build();

        commandManager.command(buildCommand
                .argument(targetArg)
                .flag(silentFlag)
                .handler(context -> {
                    CommandSender sender = context.getSender();
                    Player target = context.get(targetArg);
                    boolean silent = context.flags().isPresent(silentFlag);
                    UUID targetUuid = target.getUniqueId();
                    String targetName = target.getName();
                    String senderName = sender.getName();
                    // remove from the allowed list
                    if (allowedPlayers.contains(targetUuid)) {
                        allowedPlayers.remove(targetUuid);
                        if (silent) {
                            sender.sendMessage(BuildProtectionConfig.TARGET_OFF_SILENT.addComponent(Component.text(targetName)));
                        } else {
                            // if the sender and target are the same person
                            if (targetName.equals(senderName)) {
                                target.sendMessage(BuildProtectionConfig.SENDER_OFF.getFormatted());
                            } else {
                                target.sendMessage(BuildProtectionConfig.TARGET_OFF.addComponent(Component.text(senderName)));
                                sender.sendMessage(BuildProtectionConfig.SENDER_OFF_OTHER.addComponent(Component.text(targetName)));
                            }
                        }
                        // add to the allowed list
                    } else {
                        allowedPlayers.add(targetUuid);
                        if (silent) {
                            sender.sendMessage(BuildProtectionConfig.TARGET_ON_SILENT.addComponent(Component.text(targetName)));
                        } else {
                            // if the sender and target are the same person
                            if (targetName.equals(senderName)) {
                                target.sendMessage(BuildProtectionConfig.SENDER_ON.getFormatted());
                            } else {
                                target.sendMessage(BuildProtectionConfig.TARGET_ON.addComponent(Component.text(senderName)));
                                sender.sendMessage(BuildProtectionConfig.SENDER_ON_OTHER.addComponent(Component.text(targetName)));
                            }
                        }
                    }
                })
        );
    }
}
