package net.trustgames.lobby;

import lombok.Getter;
import net.trustgames.core.Core;
import net.trustgames.core.managers.FileManager;
import net.trustgames.lobby.hotbar.HotbarHandler;
import net.trustgames.lobby.join_leave_messages.JoinLeaveMessagesHandler;
import net.trustgames.lobby.movement.double_jump.DoubleJump;
import net.trustgames.lobby.movement.piggyback.PiggyBack;
import net.trustgames.lobby.npc.NPCHandler;
import net.trustgames.lobby.protection.LobbyGamerulesHandler;
import net.trustgames.lobby.protection.build.BlockProtectionHandler;
import net.trustgames.lobby.spawn.SpawnHandler;
import net.trustgames.lobby.spawn.commands.SetSpawnCommand;
import net.trustgames.lobby.spawn.commands.SpawnCommand;
import net.trustgames.lobby.xpbar.PlayerLevelHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

/**
 * Lobby plugin which is used on the lobbies of TrustGames.net network.
 * It extends the functionalities of Core plugin for TrustGames.net network,
 * but unlike Core shouldn't be used on any mini-games servers.
 */
public final class Lobby extends JavaPlugin {

    @Getter
    @NotNull
    private final Core core = (Core) Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Core"));

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
        // TODO improve and finish hotbar
        // TODO still using command executor
        // TODO do for other also - everytime a new Player joins, the npcs info is taken from the config and all is created again.
        // TODO dont format messages by player UUID!!!!
        // ^ do this only once and then only spawn them

        // create a data folder
        if (getDataFolder().mkdirs()) {
            getLogger().warning("Created main plugin folder");
        }

        LobbyGamerulesHandler.setGamerules();

        registerEvents();
        registerCommands();

        createConfigs();
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
        pluginManager.registerEvents(new JoinLeaveMessagesHandler(core), this);
        pluginManager.registerEvents(new NPCHandler(this), this);
        pluginManager.registerEvents(new BlockProtectionHandler(), this);
        pluginManager.registerEvents(new PlayerLevelHandler(this), this);
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
        File[] configs = new File[]{
                new File(getDataFolder(), "spawn.yml"),
                new File(getDataFolder(), "npcs.yml")
        };

        for (File file : configs){
            FileManager.createFile(this, file);
        }
    }
}
