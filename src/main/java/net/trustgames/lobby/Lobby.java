package net.trustgames.lobby;

import net.trustgames.core.Core;
import net.trustgames.core.managers.CommandManager;
import net.trustgames.core.managers.ConfigManager;
import net.trustgames.lobby.config.DefaultConfig;
import net.trustgames.lobby.hotbar.HotbarListeners;
import net.trustgames.lobby.spawn.SetSpawnCommand;
import net.trustgames.lobby.spawn.Spawn;
import net.trustgames.lobby.spawn.SpawnCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Lobby plugin which is used on the lobbies of TrustGames.net network.
 * It extends the functionalities of Core plugin for TrustGames.net network,
 * but unlike Core shouldn't be used on any mini-games servers.
 */
public final class Lobby extends JavaPlugin {

    Core core = (Core) Bukkit.getPluginManager().getPlugin("Core");

    @Override
    public void onEnable() {

        /* THINGS TO ADD:
        - double jump
         */

        // create a data folder
        if (getDataFolder().mkdirs()){
            getLogger().warning("Created config.yml");
        }

        registerEvents();
        registerCommands();

        // create config files
        ConfigManager.createConfig(new File(getDataFolder(), "spawn.yml"));
        DefaultConfig.create(getConfig()); getConfig().options().copyDefaults(true); saveConfig();

    }

    @Override
    public void onDisable() {
    }

    private void registerEvents(){
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new Spawn(this), this);
        pluginManager.registerEvents(new HotbarListeners(), this);
    }

    private void registerCommands(){
        CommandManager.registerCommand("spawn", new SpawnCommand(this));
        CommandManager.registerCommand("setspawn", new SetSpawnCommand(this));
    }
    
    
    /**
     * @return The Core plugin instance of TrustGames.net
     */
    public Core getCore() {
        return core;
    }
}
