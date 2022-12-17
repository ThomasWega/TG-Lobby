package net.trustgames.lobby;

import net.trustgames.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Lobby extends JavaPlugin {

    Core core = (Core) Bukkit.getPluginManager().getPlugin("Core");

    @Override
    public void onEnable() {
        // Plugin startup logic

        getServer().getPluginManager().registerEvents(new test(this), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Core getCore() {
        return core;
    }
}
