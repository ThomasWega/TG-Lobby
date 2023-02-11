package net.trustgames.lobby.config.movement;

import net.kyori.adventure.key.Key;
import org.bukkit.Particle;

public enum PiggyBackConfig {
    THROW_MULTIPLY(3d),
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
     * @return Value of the enum
     */
    public double getValue() {
        return ((double) value);
    }

    /**
     * @return Particle of the enum
     */
    public Particle getParticle(){
        return Particle.valueOf(String.valueOf(value));
    }

    /**
     * @return Sound key of the enum
     */
    public Key getSoundKey(){
        //noinspection PatternValidation
        return Key.key(String.valueOf(value));
    }
}
