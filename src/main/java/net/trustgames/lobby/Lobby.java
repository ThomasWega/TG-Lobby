package net.trustgames.lobby;

import cloud.commandframework.paper.PaperCommandManager;
import lombok.Getter;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.trustgames.core.Core;
import net.trustgames.lobby.hotbar.HotbarHandler;
import net.trustgames.lobby.join_leave_messages.JoinLeaveMessagesHandler;
import net.trustgames.lobby.double_jump.DoubleJumpHandler;
import net.trustgames.lobby.protection.LobbyGamerulesHandler;
import net.trustgames.lobby.protection.build.BuildProtectionCommand;
import net.trustgames.lobby.protection.build.BuildProtectionHandler;
import net.trustgames.lobby.spawn.SpawnHandler;
import net.trustgames.lobby.spawn.commands.SetSpawnCommand;
import net.trustgames.lobby.spawn.commands.SpawnCommand;
import net.trustgames.lobby.xpbar.PlayerLevelHandler;
import net.trustgames.toolkit.Toolkit;
import net.trustgames.toolkit.database.player.data.event.PlayerDataUpdateEvent;
import net.trustgames.toolkit.database.player.data.event.PlayerDataUpdateEventConfig;
import net.trustgames.toolkit.file.FileLoader;
import net.trustgames.toolkit.message_queue.RabbitManager;
import net.trustgames.toolkit.message_queue.event.RabbitEvent;
import net.trustgames.toolkit.message_queue.event.RabbitEventBus;
import net.trustgames.toolkit.message_queue.event.RabbitEventManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Lobby plugin which is used on the lobbies of TrustGames.net network.
 * It extends the functionalities of Core plugin for TrustGames.net network,
 * but unlike Core shouldn't be used on any mini-games servers.
 */
public final class Lobby extends JavaPlugin implements Listener {

    public static ComponentLogger LOGGER;
    @Getter
    private Core core;
    @Getter
    private Toolkit toolkit;
    @Getter
    private PaperCommandManager<CommandSender> commandManager;

    @Getter
    private RabbitEventManager rabbitEventManager;

    @Getter
    private RabbitEventBus<RabbitEvent> rabbitEventBus;

    @Override
    public void onEnable() {
        // get the core instance
        Plugin corePlugin = Bukkit.getPluginManager().getPlugin("TG-Core");
        if (corePlugin == null) {
            throw new RuntimeException("Failed to load TG-Core plugin for " + this.getName());
        }
        LOGGER = getComponentLogger();
        core = (Core) corePlugin;
        toolkit = core.getToolkit();

        new LobbyGamerulesHandler();

        /* THINGS TO ADD:
        - daily rewards
        - level rewards
        - cosmetics
         */

        // create a data folder
        if (getDataFolder().mkdir()) {
            LOGGER.warn("Created main plugin folder {}", getDataFolder().getAbsolutePath());
        }

        registerEvents();
        registerCommands();

        createConfigs();
    }

    @Override
    public void onDisable() {
    }

    private void registerEvents() {
        new SpawnHandler(this);
        new HotbarHandler(this);
        new DoubleJumpHandler(this);
        new BuildProtectionHandler(this);
        new JoinLeaveMessagesHandler(this);
        new PlayerLevelHandler(this);

        RabbitManager rabbitManager = toolkit.getRabbitManager();
        if (rabbitManager != null) {
            registerRabbitEvents();
        }
    }

    private void registerRabbitEvents() {
        this.rabbitEventManager = core.getRabbitEventManager();
        this.rabbitEventBus = core.getRabbitEventBus();

        rabbitEventBus.subscribe(PlayerDataUpdateEvent.class, new PlayerDataUpdateEventConfig().config(), new PlayerLevelHandler(this));
    }

    private void registerCommands() {
        commandManager = core.getCommandManager();

        new BuildProtectionCommand(this);
        new SetSpawnCommand(this);
        new SpawnCommand(this);
    }

    private void createConfigs() {
        /*
         1. Name of the file
         2. Folder of the file
        */
        Map<String, File> configsMap = new HashMap<>(Map.of(
                "spawn.yml", getDataFolder()
        ));

        configsMap.forEach((configName, configDir) -> {
            try {
                FileLoader.loadFile(this.getClassLoader(), configDir, configName);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load config " + configName + "from resources file", e);
            }
        });
    }
}
