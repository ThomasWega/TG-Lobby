package net.trustgames.lobby.movement;

import com.destroystokyo.paper.ParticleBuilder;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import net.trustgames.lobby.Lobby;
import net.trustgames.lobby.config.movement.PiggyBackConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class PiggyBack implements Listener {

    private final Lobby lobby;

    public PiggyBack(Lobby lobby) {
        this.lobby = lobby;
    }

    private static final ParticleBuilder particle = new ParticleBuilder(
            PiggyBackConfig.PARTICLE.getParticle())
            .count((int) PiggyBackConfig.PARTICLE_COUNT.getValue());

    private static final Sound sound = Sound.sound(
            PiggyBackConfig.SOUND.getSoundKey(),
            Sound.Source.AMBIENT,
            (float) PiggyBackConfig.SOUND_VOLUME.getValue(),
            (float) PiggyBackConfig.SOUND_PITCH.getValue());

    @EventHandler
    private void onPlayerRightClick(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        // TODO if player is in vanish or have this disabled check

        if (entity instanceof Player clicked) {
            ride(player, clicked);
        }
    }

    @EventHandler
    private void onPlayerLeftClick(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity target = event.getEntity();

        if (damager instanceof Player player && target instanceof Player passenger) {

            if (!damager.getPassengers().contains(target)) return;

            throwPassenger(player, passenger);
        }

    }

    /**
     * Add the players clicked on as passengers. If the player already
     * has 1 passenger, the next passenger will be set as the passenger of
     * the last passenger on the player
     *
     * @param player Player holding the passengers
     * @param clicked Player that was clicked and will become a passenger now
     */
    private void ride(Player player, Player clicked) {
        List<Entity> passengerList = player.getPassengers();

        if (passengerList.isEmpty() || passengerList.size() == 1)
            player.addPassenger(clicked);
        else {
            Entity passengerLast = passengerList.get(passengerList.size() - 1);
            passengerLast.addPassenger(clicked);
        }
    }

    /**
     * Throw the passenger in the air in the direction the damager is facing.
     * Works by multiplying vectors, but sometimes doesn't throw far enough
     * Player can have more passengers, but can only throw one at time. The rest
     * of the players will still remain his passengers
     *
     * @param player Player that damaged the passenger
     * @param passenger Player that is sitting on the player
     */
    private void throwPassenger(Player player, Player passenger) {
        player.removePassenger(passenger);

        Location loc = player.getLocation();
        loc.setPitch(0);
        Vector vec = loc.getDirection()
                .multiply(PiggyBackConfig.THROW_MULTIPLY.getValue());
        passenger.setVelocity(vec);

        particle(passenger);

        // if more player are on each other, set the remaining players as passengers
        List<Entity> passengerList = passenger.getPassengers();
        if (!passengerList.isEmpty()) {
            player.addPassenger(passengerList.get(0));
        }
    }

    /**
     * When passenger is in the air, spawn a particle on his location every 2 ticks
     * The max time to spawn particles is 3 seconds, meaning if the player is in air for
     * longer than 3 seconds, the particles will stop spawning.
     *
     * @param passenger Passenger to get location where to spawn particles at
     */
    private void particle(Player passenger) {
        Audience.audience(Bukkit.getOnlinePlayers()).playSound(sound, passenger);
        new BukkitRunnable() {

            int i = 0;
            @Override
            public void run() {
                if (i == 30) cancel();
                if (!(passenger.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR)) cancel();

                particle.location(passenger.getLocation()).spawn();
                i++;
            }
        }.runTaskTimer(lobby, 2, 2);
    }
}