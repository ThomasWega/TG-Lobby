package net.trustgames.lobby.movement.double_jump;

import net.kyori.adventure.key.Key;
import org.bukkit.Particle;

public enum DoubleJumpConfig {
    HOR(0.8d),
    VER(0.8d),
    HOR_RUN(0.85d),
    VER_RUN(0.6d),
    PARTICLE("ELECTRIC_SPARK"),
    PARTICLE_COUNT(5d),
    SOUND("entity.bat.takeoff"),
    SOUND_VOLUME(1d),
    SOUND_PITCH(1d);

    private final Object value;

    DoubleJumpConfig(Object value) {
        this.value = value;
    }

    /**
     * @return Double value of the enum
     */
    public double getDouble() {
        return ((double) value);
    }

    /**
     * @return Particle of the enum
     */
    public Particle getParticle() {
        return Particle.valueOf(String.valueOf(value));
    }

    /**
     * @return Sound key of the enum
     */
    public Key getSoundKey() {
        //noinspection PatternValidation
        return Key.key(String.valueOf(value));
    }
}
