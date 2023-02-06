package net.trustgames.lobby.config;

public enum DoubleJumpConfig {
    HOR(0.8d),
    VER(0.8d),
    HOR_RUN(0.85d),
    VER_RUN(0.6d);

    private final double value;

    DoubleJumpConfig(double value) {
        this.value = value;
    }

    /**
     * @return Value of the enum
     */
    public double getValue() {
        return value;
    }
}
