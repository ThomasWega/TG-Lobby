package net.trustgames.lobby;

import cloud.commandframework.paper.PaperCommandManager;
import com.destroystokyo.paper.utils.PaperPluginLogger;
import lombok.Getter;
import net.trustgames.core.Core;
import net.trustgames.core.managers.FileManager;
import net.trustgames.lobby.hotbar.HotbarHandler;
import net.trustgames.lobby.join_leave_messages.JoinLeaveMessagesHandler;
import net.trustgames.lobby.movement.double_jump.DoubleJump;
import net.trustgames.lobby.movement.piggyback.PiggyBack;
import net.trustgames.lobby.npc.NPCHandler;
import net.trustgames.lobby.protection.LobbyGamerulesHandler;
import net.trustgames.lobby.protection.build.BuildProtectionCommand;
import net.trustgames.lobby.protection.build.BuildProtectionHandler;
import net.trustgames.lobby.spawn.SpawnHandler;
import net.trustgames.lobby.spawn.commands.SetSpawnCommand;
import net.trustgames.lobby.spawn.commands.SpawnCommand;
import net.trustgames.lobby.xpbar.PlayerLevelHandler;
import net.trustgames.toolkit.Toolkit;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Lobby plugin which is used on the lobbies of TrustGames.net network.
 * It extends the functionalities of Core plugin for TrustGames.net network,
 * but unlike Core shouldn't be used on any mini-games servers.
 */
public final class Lobby extends JavaPlugin {

    public static final Logger LOGGER = PaperPluginLogger.getLogger("Lobby");
    @Getter
    private Core core;
    @Getter
    private Toolkit toolkit;
    @Getter
    private PaperCommandManager<CommandSender> commandManager;

    @Override
    public void onEnable() {
        new LobbyGamerulesHandler();

        /* THINGS TO ADD:
        - daily rewards
        - level rewards
        - cosmetics
         */

        /* SIDE ADDITIONS
        - hover on player name in join/leave messages, add info
        - add permissions for join/leave messages
        - piggyback ignore when on vanish
        - piggyback toggle
         */


        // TODO maybe move the npcHandler to core plugin???
        // TODO improve and finish hotbar
        // TODO do for other also - everytime a new Player joins, the npcs info is taken from the config and all is created again.
        //    ^  do this only once and then only spawn them
        // TODO custom messages for cloud

        // TODO commands add cooldown (first make CooldownManager per instance)

        // TEST still using command executor
        // TEST /setspawn

        // get the core instance
        core = (Core) Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Core"));
        toolkit = core.getToolkit();

        // create a data folder
        if (getDataFolder().mkdirs()) {
            getLogger().warning("Created main plugin folder");
        }

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
        pluginManager.registerEvents(new NPCHandler(this), this);
        pluginManager.registerEvents(new BuildProtectionHandler(), this);
        pluginManager.registerEvents(new JoinLeaveMessagesHandler(), this);
        pluginManager.registerEvents(new PlayerLevelHandler(toolkit), this);
    }

    private void registerCommands() {

        commandManager = core.getCommandManager();

        new BuildProtectionCommand(commandManager);
        new SetSpawnCommand(this);
        new SpawnCommand(this);
    }

    private void createConfigs() {
        File[] configs = new File[]{
                new File(getDataFolder(), "spawn.yml"),
                new File(getDataFolder(), "npcs.yml")
        };

        for (File file : configs) {
            FileManager.createFile(this, file);
        }
    }
}
