package net.trustgames.lobby.gamerule;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;

public class LobbyGamerules {

    public static void setGamerules() {

        for (World world : Bukkit.getServer().getWorlds()) {

            if (world != null) {

                world.setGameRule(GameRule.FIRE_DAMAGE, false);
                world.setGameRule(GameRule.FALL_DAMAGE, false);
                world.setDifficulty(Difficulty.PEACEFUL);
            }
        }
    }
}
