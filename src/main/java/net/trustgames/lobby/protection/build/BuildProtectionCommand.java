package net.trustgames.lobby.protection.build;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.Command;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.flags.CommandFlag;
import cloud.commandframework.bukkit.parsers.PlayerArgument;
import cloud.commandframework.paper.PaperCommandManager;
import net.kyori.adventure.text.Component;
import net.trustgames.core.gui.GUIManager;
import net.trustgames.core.gui.type.InventoryHandler;
import net.trustgames.core.gui.type.PlayerGUI;
import net.trustgames.lobby.Lobby;
import net.trustgames.toolkit.config.PermissionConfig;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.Map;
import java.util.UUID;

public class BuildProtectionCommand {
    private static final Map<UUID, InventoryHandler> allowedPlayersMap = BuildProtectionAllowedPlayersMap.getAllowedMap();
    private static final Map<UUID, GameMode> gamemodesMap = BuildProtectionAllowedPlayersMap.GameModesMap.getGamemodesMap();
    private final PaperCommandManager<CommandSender> commandManager;
    private final GUIManager guiManager;

    public BuildProtectionCommand(Lobby lobby) {
        this.commandManager = lobby.getCommandManager();
        this.guiManager = lobby.getCore().getGuiManager();

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

                    // player is in build mode already
                    if (allowedPlayersMap.containsKey(uuid)) {
                        tryRestorePlayerGUI(player);
                        restoreGameMode(player);
                        player.sendMessage(BuildProtectionConfig.SENDER_OFF.getFormatted());
                    } else {
                        trySavePlayerGUI(player);
                        saveGameMode(player);
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

                    // player is in build mode already
                    if (allowedPlayersMap.containsKey(targetUuid)) {
                        tryRestorePlayerGUI(target);
                        restoreGameMode(target);

                        if (silent) {
                            sender.sendMessage(BuildProtectionConfig.TARGET_OFF_SILENT.addComponent(Component.text(targetName)));
                            return;

                        }
                        // if the sender and target are the same person
                        if (targetName.equalsIgnoreCase(senderName)) {
                            target.sendMessage(BuildProtectionConfig.SENDER_OFF.getFormatted());
                            return;
                        }

                        target.sendMessage(BuildProtectionConfig.TARGET_OFF.addComponent(Component.text(senderName)));
                        sender.sendMessage(BuildProtectionConfig.SENDER_OFF_OTHER.addComponent(Component.text(targetName)));

                    } else {
                        trySavePlayerGUI(target);
                        saveGameMode(target);

                        if (silent) {
                            sender.sendMessage(BuildProtectionConfig.TARGET_ON_SILENT.addComponent(Component.text(targetName)));
                            return;
                        }

                        // if the sender and target are the same person
                        if (targetName.equalsIgnoreCase(senderName)) {
                            target.sendMessage(BuildProtectionConfig.SENDER_ON.getFormatted());
                            return;
                        }

                        target.sendMessage(BuildProtectionConfig.TARGET_ON.addComponent(Component.text(senderName)));
                        sender.sendMessage(BuildProtectionConfig.SENDER_ON_OTHER.addComponent(Component.text(targetName)));
                    }
                })
        );
    }


    private void trySavePlayerGUI(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        // get PlayerGUI (if any set)
        InventoryHandler inventoryHandler = guiManager.getActiveInventories().get(playerInventory);
        if (inventoryHandler == null) return;

        UUID uuid = player.getUniqueId();
        // save the PlayerGUI and unregister it (to allow player to use his inventory)
        allowedPlayersMap.put(uuid, inventoryHandler);
        guiManager.unregisterInventory(playerInventory);
        playerInventory.clear();
    }

    private void tryRestorePlayerGUI(Player player) {
        UUID uuid = player.getUniqueId();
        // get players stored PlayerGUI (if any)
        InventoryHandler inventoryHandler = allowedPlayersMap.get(uuid);
        if (inventoryHandler == null) return;

        PlayerInventory playerInventory = player.getInventory();
        PlayerGUI playerGUI = (PlayerGUI) inventoryHandler;
        // register the gui and fill the player inventory with items
        guiManager.registerInventory(playerInventory, playerGUI);
        playerGUI.fill();
        allowedPlayersMap.remove(uuid);
    }


    private void saveGameMode(Player player) {
        gamemodesMap.put(player.getUniqueId(), player.getGameMode());
        player.setGameMode(GameMode.CREATIVE);
    }

    private void restoreGameMode(Player player) {
        GameMode gameMode = gamemodesMap.get(player.getUniqueId());
        if (gameMode == null) return;

        player.setGameMode(gameMode);
    }
}
