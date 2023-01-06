package net.trustgames.lobby;

import net.trustgames.core.Core;
import net.trustgames.core.managers.CommandManager;
import net.trustgames.core.managers.ConfigManager;
import net.trustgames.core.managers.EventManager;
import net.trustgames.core.managers.FolderManager;
import net.trustgames.lobby.config.DefaultConfig;
import net.trustgames.lobby.hotbar.HotbarListeners;
import net.trustgames.lobby.spawn.SetSpawnCommand;
import net.trustgames.lobby.spawn.Spawn;
import net.trustgames.lobby.spawn.SpawnCommand;
import net.trustgames.lobby.tablist.TablistHeaderFooter;
import net.trustgames.lobby.tablist.TablistPrefix;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Lobby extends JavaPlugin {

    Core core = (Core) Bukkit.getPluginManager().getPlugin("Core");

    @Override
    public void onEnable() {

        /* THINGS TO ADD:
        - double jump
        - command anti-spam
         */


        // TODO sort tablist


        // create data folder
        FolderManager.createDataFolder(getDataFolder());

        // register commands
        CommandManager.registerCommand("spawn", new SpawnCommand(this));
        CommandManager.registerCommand("setspawn", new SetSpawnCommand(this));

        // register events
        EventManager.registerEvent(new Spawn(this), this);
        EventManager.registerEvent(new HotbarListeners(), this);
        EventManager.registerEvent(new TablistHeaderFooter(this), this);
        EventManager.registerEvent(new TablistPrefix(), this);

        // create config files
        ConfigManager.createConfig(new File(getDataFolder(), "spawn.yml"));
        DefaultConfig.create(getConfig()); getConfig().options().copyDefaults(true); saveConfig();

    }

    @Override
    public void onDisable() {
    }

    public Core getCore() {
        return core;
    }
}
