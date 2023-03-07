package net.trustgames.lobby.protection.build;

import net.kyori.adventure.text.Component;
import net.trustgames.core.cache.EntityCache;
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
import java.util.UUID;

public final class BlockProtectionHandler implements Listener, CommandExecutor {

    private static final Set<UUID> allowedPlayers = new HashSet<>();

    /**
     * If player isn't in the map of allowed players to interact with blocks,
     * the event is cancelled.
     */
    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UUID uuid = EntityCache.getUUID(player);

        if (!allowedPlayers.contains(uuid))
            event.setCancelled(true);
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = EntityCache.getUUID(player);
        if (!allowedPlayers.contains(uuid))
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

            UUID uuid = EntityCache.getUUID(player);
            Player target;
            if (args.length >= 1) {
                for (String arg : args) {
                    target = Bukkit.getPlayer(arg);

                    if (target == null) {
                        player.sendMessage(CommandConfig.COMMAND_PLAYER_OFFLINE.addName(Component.text(arg)));
                        return true;
                    }

                    UUID uuid2 = EntityCache.getUUID(target);
                    if (allowedPlayers.contains(uuid2)) {
                        allowedPlayers.remove(uuid2);
                        target.sendMessage(BuildProtectionConfig.OFF.getMessage());
                        player.sendMessage(BuildProtectionConfig.OFF_OTHER.addName(Component.text(target.getName())));
                    } else {
                        allowedPlayers.add(uuid2);
                        target.sendMessage(BuildProtectionConfig.ON.getMessage());
                        player.sendMessage(BuildProtectionConfig.ON_OTHER.addName(Component.text(target.getName())));
                    }
                }
            } else {
                if (allowedPlayers.contains(uuid)) {
                    allowedPlayers.remove(uuid);
                    player.sendMessage(BuildProtectionConfig.OFF.getMessage());
                } else {
                    allowedPlayers.add(uuid);
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
        Player player = event.getPlayer();
        UUID uuid = EntityCache.getUUID(player);

        allowedPlayers.remove(uuid);
    }
}
