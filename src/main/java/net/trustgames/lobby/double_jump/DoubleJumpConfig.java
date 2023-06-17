package net.trustgames.lobby.double_jump;

import lombok.Getter;

public enum DoubleJumpConfig {
    HOR(0.8d),
    VER(0.8d),
    HOR_RUN(0.85d),
    VER_RUN(0.6d);

    @Getter
    private final double value;

    DoubleJumpConfig(double value) {
        this.value = value;
    }
}
