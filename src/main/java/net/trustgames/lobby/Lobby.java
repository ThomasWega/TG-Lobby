package net.trustgames.lobby;

import net.trustgames.core.Core;
import net.trustgames.core.managers.CommandManager;
import net.trustgames.core.managers.ConfigManager;
import net.trustgames.core.managers.EventManager;
import net.trustgames.core.managers.FolderManager;
import net.trustgames.lobby.hotbar.HotbarListeners;
import net.trustgames.lobby.spawn.SetSpawnCommand;
import net.trustgames.lobby.spawn.Spawn;
import net.trustgames.lobby.spawn.SpawnCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Lobby extends JavaPlugin {

    Core core;

    @Override
    public void onEnable() {

        /* THINGS TO ADD:
        - double jump
         */

        // create data folder
        FolderManager.createDataFolder(getDataFolder());

        // register commands
        CommandManager.registerCommand("spawn", new SpawnCommand(this));
        CommandManager.registerCommand("setspawn", new SetSpawnCommand(this));

        // register events
        EventManager.registerEvent(new Spawn(this), this);
        EventManager.registerEvent(new HotbarListeners(), this);

        // create config files
        ConfigManager.createConfig(new File(getDataFolder(), "spawn.yml"));

    }

    @Override
    public void onDisable() {
    }

    public Core getCore() {
        return core;
    }
}
