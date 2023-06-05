package net.trustgames.lobby.movement.piggyback;

import lombok.Getter;

public enum PiggyBackConfig {
    THROW_MULTIPLY(2d),
    THROW_Y(0.7d);

    @Getter
    private final double value;

    PiggyBackConfig(double value) {
        this.value = value;
    }
}
