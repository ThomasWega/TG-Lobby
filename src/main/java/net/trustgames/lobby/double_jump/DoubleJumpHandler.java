package net.trustgames.lobby.double_jump;

import com.destroystokyo.paper.ParticleBuilder;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.trustgames.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class DoubleJumpHandler implements Listener {
    private final Lobby lobby;
    private final Set<UUID> cooldowns = new HashSet<>();
    private static final Sound sound = Sound.sound(
            Key.key("block.note_block.flute"),
            Sound.Source.AMBIENT,
            1, 1
    );

    private static final ParticleBuilder particleBuilder = new ParticleBuilder(Particle.ELECTRIC_SPARK)
            .count(5)
            .offset(0.5d, 0.5d, 0.5d);

    public DoubleJumpHandler(Lobby lobby) {
        this.lobby = lobby;
        Bukkit.getPluginManager().registerEvents(this, lobby);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void setFly(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.setAllowFlight(true);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void setVelocity(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR
                || player.isFlying() || cooldowns.contains(uuid) || player.getVehicle() != null) return;

        cooldowns.add(uuid);

        event.setCancelled(true);

        player.setAllowFlight(false);
        player.setFlying(false);

        double hor_run = DoubleJumpConfig.HOR_RUN.getValue();
        double ver_run = DoubleJumpConfig.VER_RUN.getValue();
        double hor = DoubleJumpConfig.HOR.getValue();
        double ver = DoubleJumpConfig.VER.getValue();

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

        Audience.audience(Bukkit.getOnlinePlayers())
                .playSound(sound, player);

        particleBuilder.location(player.getLocation())
                .spawn();

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
    public void removeFromSet(@NotNull Player player) {
        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (!(player.getLocation().getBlock().getRelative(BlockFace.DOWN, 2)
                        .getType() == Material.AIR) || i >= 7) {
                    cooldowns.remove(player.getUniqueId());
                    player.setAllowFlight(true);
                    cancel();
                }
                i++;
            }
        }.runTaskTimer(lobby, 20, 7);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {
        cooldowns.remove(event.getPlayer().getUniqueId());
    }
}
