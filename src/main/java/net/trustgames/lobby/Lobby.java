package net.trustgames.lobby;

import net.trustgames.core.Core;
import net.trustgames.core.managers.ConfigManager;
import net.trustgames.lobby.hotbar.HotbarHandler;
import net.trustgames.lobby.join_leave_messages.JoinLeaveMessagesHandler;
import net.trustgames.lobby.movement.double_jump.DoubleJump;
import net.trustgames.lobby.movement.piggyback.PiggyBack;
import net.trustgames.lobby.npc.NPCConfig;
import net.trustgames.lobby.npc.NPCHandler;
import net.trustgames.lobby.protection.LobbyGamerulesHandler;
import net.trustgames.lobby.protection.build.BlockProtectionHandler;
import net.trustgames.lobby.spawn.SpawnHandler;
import net.trustgames.lobby.spawn.commands.SetSpawnCommand;
import net.trustgames.lobby.spawn.commands.SpawnCommand;
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

    private final Core core = (Core) Bukkit.getPluginManager().getPlugin("Core");

    @Override
    public void onEnable() {

        /* THINGS TO ADD:
        - daily rewards
         */

        /* SIDE ADDITIONS
        - hover on player name in join/leave messages, add info
        - add permissions for join/leave messages
        - piggyback ignore when on vanish
        - piggyback toggle
         */

        // TODO maybe move the npcHandler to core plugin???

        // create a data folder
        if (getDataFolder().mkdirs()) {
            getLogger().warning("Created main plugin folder");
        }

        LobbyGamerulesHandler.setGamerules();

        registerEvents();
        registerCommands();

        createConfigs();
        createConfigsDefaults();
    }

    @Override
    public void onDisable() {
    }

    private void registerEvents() {
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new SpawnHandler(this), this);
        pluginManager.registerEvents(new HotbarHandler(), this);
        pluginManager.registerEvents(new DoubleJump(this), this);
        pluginManager.registerEvents(new PiggyBack(this), this);
        pluginManager.registerEvents(new JoinLeaveMessagesHandler(), this);
        pluginManager.registerEvents(new NPCHandler(this), this);
        pluginManager.registerEvents(new BlockProtectionHandler(), this);
    }

    private void registerCommands() {

        // List of command to register
        HashMap<PluginCommand, CommandExecutor> cmdList = new HashMap<>();
        cmdList.put(getCommand("setspawn"), new SetSpawnCommand(this));
        cmdList.put(getCommand("spawn"), new SpawnCommand(this));
        cmdList.put(getCommand("build"), new BlockProtectionHandler());

        for (PluginCommand cmd : cmdList.keySet()) {
            cmd.setExecutor(cmdList.get(cmd));
        }
    }

    private void createConfigs() {
        ConfigManager.createConfig(new File(getDataFolder(), "spawn.yml"));
        ConfigManager.createConfig(new File(getDataFolder(), "npcs.yml"));
    }

    private void createConfigsDefaults() {
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
