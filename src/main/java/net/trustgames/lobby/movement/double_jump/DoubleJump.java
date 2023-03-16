package net.trustgames.lobby.movement.double_jump;

import com.destroystokyo.paper.ParticleBuilder;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import net.trustgames.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
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

public final class DoubleJump implements Listener {

    private static final ParticleBuilder particle = new ParticleBuilder(
            DoubleJumpConfig.PARTICLE.getParticle())
            .count((int) DoubleJumpConfig.PARTICLE_COUNT.getDouble())
            .offset(0.5d, 0.5d, 0.5d);
    private static final Sound sound = Sound.sound(
            DoubleJumpConfig.SOUND.getSoundKey(),
            Sound.Source.AMBIENT,
            (float) DoubleJumpConfig.SOUND_VOLUME.getDouble(),
            (float) DoubleJumpConfig.SOUND_PITCH.getDouble());
    private final Lobby lobby;
    private final Set<String> cooldowns = new HashSet<>();

    public DoubleJump(Lobby lobby) {
        this.lobby = lobby;
    }

    @EventHandler
    public void setFly(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.setAllowFlight(true);
    }

    @EventHandler
    public void setVelocity(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR
                || player.isFlying() || cooldowns.contains(playerName) || player.getVehicle() != null) return;

        cooldowns.add(playerName);

        event.setCancelled(true);

        player.setAllowFlight(false);
        player.setFlying(false);

        double hor_run = DoubleJumpConfig.HOR_RUN.getDouble();
        double ver_run = DoubleJumpConfig.VER_RUN.getDouble();
        double hor = DoubleJumpConfig.HOR.getDouble();
        double ver = DoubleJumpConfig.VER.getDouble();

        if (player.isSprinting()) {
            player.setVelocity(player.getLocation().getDirection().normalize()
                    .multiply(hor_run)
                    .setY(ver_run)
                    .normalize());
        } else {
            player.setVelocity(player.getLocation().getDirection().normalize()
                    .multiply(hor)
                    .setY(ver)
                    .normalize());
        }

        Audience.audience(Bukkit.getOnlinePlayers()).playSound(sound, player);
        particle.location(player.getLocation()).spawn();

        removeFromSet(player);
    }

    /**
     * Remove the player from the cooldowns list if the block two blocks far facing down
     * from his location isn't air or if it has been more than 3 seconds till his last double jump.
     * This is done so the player can't spam double jump in the air, but at the
     * same time he can jump on the ground and still be able to double jump.
     *
     * @param player Player to remove from the set
     */
    public void removeFromSet(Player player) {
        new BukkitRunnable() {
            final String playerName = player.getName();
            int i = 0;

            @Override
            public void run() {
                if (!(player.getLocation().getBlock().getRelative(BlockFace.DOWN, 2)
                        .getType() == Material.AIR) || i >= 7) {
                    cooldowns.remove(playerName);
                    player.setAllowFlight(true);
                    cancel();
                }
                i++;
            }
        }.runTaskTimerAsynchronously(lobby, 20, 7);
    }

    @EventHandler
    public void removePlayer(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        cooldowns.remove(playerName);
    }
}
