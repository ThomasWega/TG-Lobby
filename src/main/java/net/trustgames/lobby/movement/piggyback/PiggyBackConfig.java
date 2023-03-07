package net.trustgames.lobby.movement.piggyback;

import net.kyori.adventure.key.Key;
import org.bukkit.Particle;

public enum PiggyBackConfig {
    THROW_MULTIPLY(2d),
    THROW_Y(0.7d),
    PARTICLE("CRIT"),
    PARTICLE_COUNT(3d),
    SOUND("entity.ghast.shoot"),
    SOUND_VOLUME(1d),
    SOUND_PITCH(1d);

    private final Object value;

    PiggyBackConfig(Object value) {
        this.value = value;
    }

    /**
     * @return Double value of the enum
     */
    public final double getDouble() {
        return ((double) value);
    }

    /**
     * @return Particle of the enum
     */
    public final Particle getParticle() {
        return Particle.valueOf(String.valueOf(value));
    }

    /**
     * @return Sound key of the enum
     */
    public final Key getSoundKey() {
        //noinspection PatternValidation
        return Key.key(String.valueOf(value));
    }
}
