package net.trustgames.lobby;

import net.trustgames.core.Core;
import net.trustgames.core.managers.ConfigManager;
import net.trustgames.lobby.config.DefaultConfig;
import net.trustgames.lobby.double_jump.DoubleJump;
import net.trustgames.lobby.gamerules.LobbyGamerules;
import net.trustgames.lobby.hotbar.HotbarListeners;
import net.trustgames.lobby.join_leave.JoinLeaveMessages;
import net.trustgames.lobby.npcs.NPCConfig;
import net.trustgames.lobby.npcs.SpawnNPCS;
import net.trustgames.lobby.spawn.SetSpawnCommand;
import net.trustgames.lobby.spawn.Spawn;
import net.trustgames.lobby.spawn.SpawnCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;

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
        - daily rewards
         */

        // TODO polish NPC
        // TODO better NMS remap

        // create a data folder
        if (getDataFolder().mkdirs()){
            getLogger().warning("Created main plugin folder");
        }

        LobbyGamerules.setGamerules();

        registerEvents();
        registerCommands();

        createConfigs();
        createConfigsDefaults();
    }

    @Override
    public void onDisable() {
    }

    private void registerEvents(){
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new Spawn(this), this);
        pluginManager.registerEvents(new HotbarListeners(), this);
        pluginManager.registerEvents(new DoubleJump(this), this);
        pluginManager.registerEvents(new JoinLeaveMessages(this), this);
        pluginManager.registerEvents(new SpawnNPCS(this, core), this);
    }

    private void registerCommands(){

        // List of command to register
        HashMap<PluginCommand, CommandExecutor> cmdList = new HashMap<>();
        cmdList.put(getCommand("setspawn"), new SetSpawnCommand(this));
        cmdList.put(getCommand("spawn"), new SpawnCommand(this));

        for (PluginCommand cmd : cmdList.keySet()) {
            cmd.setExecutor(cmdList.get(cmd));
        }
    }

    private void createConfigs(){
        ConfigManager.createConfig(new File(getDataFolder(), "spawn.yml"));
        ConfigManager.createConfig(new File(getDataFolder(), "npcs.yml"));
    }

    private void createConfigsDefaults(){
        DefaultConfig.create(getConfig()); getConfig().options().copyDefaults(true); saveConfig();

        NPCConfig npcConfig = new NPCConfig(this);
        npcConfig.createDefaults();
    }
    
    
    /**
     * @return The Core plugin instance of TrustGames.net
     */
    public Core getCore() {
        return core;
    }
}
