package net.trustgames.lobby.double_jump;

import net.trustgames.core.managers.PlayerManager;
import net.trustgames.lobby.Lobby;
import net.trustgames.lobby.settings.LobbySettings;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DoubleJump implements Listener {

    private final Lobby lobby;

    public DoubleJump(Lobby lobby) {
        this.lobby = lobby;
    }

    private final Set<UUID> cooldowns = new HashSet<>();

    @EventHandler
    public void setFly(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.setAllowFlight(true);
    }

    @EventHandler
    public void setVelocity(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        UUID uuid = PlayerManager.getUUID(player);

        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR
                || player.isFlying() || cooldowns.contains(uuid)) return;

        cooldowns.add(uuid);

        event.setCancelled(true);

        player.setAllowFlight(false);
        player.setFlying(false);

        double hor_run = LobbySettings.DOUBLE_JUMP_HOR_RUN;
        double ver_run = LobbySettings.DOUBLE_JUMP_VER_RUN;
        double hor = LobbySettings.DOUBLE_JUMP_HOR;
        double ver = LobbySettings.DOUBLE_JUMP_VER;

        if (player.isSprinting()){
            player.setVelocity(player.getLocation().getDirection().multiply(hor_run).setY(ver_run));
        }
        else{
            player.setVelocity(player.getLocation().getDirection().multiply(hor).setY(ver));
        }

        player.playSound(player, Sound.ENTITY_BAT_TAKEOFF, 0.7f, 1.5f);
        player.spawnParticle(Particle.ELECTRIC_SPARK, player.getLocation(), 5, 0.5, 0.5, 0.5);

        removeFromSet(player);
    }

    public void removeFromSet(Player player){
        UUID uuid = PlayerManager.getUUID(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!(player.getLocation().getBlock().getRelative(BlockFace.DOWN, 2).getType() == Material.AIR)){
                    cooldowns.remove(uuid);
                    player.setAllowFlight(true);
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(lobby, 20, 7);
    }

    @EventHandler
    public void removePlayer(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = PlayerManager.getUUID(player);

        cooldowns.remove(uuid);

    }
}
