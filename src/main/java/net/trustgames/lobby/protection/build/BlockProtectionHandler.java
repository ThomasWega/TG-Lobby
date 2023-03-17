package net.trustgames.lobby.protection.build;

import net.kyori.adventure.text.Component;
import net.trustgames.core.config.CommandConfig;
import net.trustgames.lobby.config.LobbyPermissionConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public final class BlockProtectionHandler implements Listener, CommandExecutor {

    private final Set<String> allowedPlayers = new HashSet<>();

    /**
     * If player isn't in the map of allowed players to interact with blocks,
     * the event is cancelled.
     */
    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        String playerName = event.getPlayer().getName();
        if (!allowedPlayers.contains(playerName))
            event.setCancelled(true);
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        String playerName = event.getPlayer().getName();
        if (!allowedPlayers.contains(playerName))
            event.setCancelled(true);
    }

    /*
    If the player has the needed permission, check if he is in the
    map of allowed players to interact with blocks, if true, remove him.
    If he isn't in the map yet, add him there.
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission(LobbyPermissionConfig.STAFF.permission)) {
                player.sendMessage(CommandConfig.COMMAND_NO_PERM.getText());
                return true;
            }
            String playerName = player.getName();
            if (args.length >= 1) {
                for (String arg : args) {
                    Player target = Bukkit.getPlayer(arg);

                    if (target == null) {
                        player.sendMessage(CommandConfig.COMMAND_PLAYER_OFFLINE.addComponent(Component.text(arg)));
                        return true;
                    }

                    String targetName = target.getName();
                    if (allowedPlayers.contains(targetName)) {
                        allowedPlayers.remove(targetName);
                        target.sendMessage(BuildProtectionConfig.OFF.getMessage());
                        player.sendMessage(BuildProtectionConfig.OFF_OTHER.addComponent(Component.text(targetName)));
                    } else {
                        allowedPlayers.add(targetName);
                        target.sendMessage(BuildProtectionConfig.ON.getMessage());
                        player.sendMessage(BuildProtectionConfig.ON_OTHER.addComponent(Component.text(targetName)));
                    }
                }
            } else {
                if (allowedPlayers.contains(playerName)) {
                    allowedPlayers.remove(playerName);
                    player.sendMessage(BuildProtectionConfig.OFF.getMessage());
                } else {
                    allowedPlayers.add(playerName);
                    player.sendMessage(BuildProtectionConfig.ON.getMessage());
                }
            }
        } else {
            sender.sendMessage(CommandConfig.COMMAND_PLAYER_ONLY.value.toString());
        }
        return true;
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        allowedPlayers.remove(playerName);
    }
}
