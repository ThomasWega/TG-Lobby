package net.trustgames.lobby.protection;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;

public final class LobbyGamerulesHandler {

    public static void setGamerules() {

        for (World world : Bukkit.getServer().getWorlds()) {

            if (world != null) {

                world.setGameRule(GameRule.DO_FIRE_TICK, false);
                world.setGameRule(GameRule.DROWNING_DAMAGE, false);
                world.setGameRule(GameRule.FALL_DAMAGE, false);
                world.setGameRule(GameRule.FIRE_DAMAGE, false);
                world.setGameRule(GameRule.FREEZE_DAMAGE, false);
                world.setGameRule(GameRule.MOB_GRIEFING, false);
                world.setGameRule(GameRule.NATURAL_REGENERATION, true);
                world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
            }
        }
    }
}
