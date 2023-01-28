package net.trustgames.lobby.npcs;

import net.minecraft.server.level.ServerPlayer;
import net.trustgames.core.Core;
import net.trustgames.core.managers.NPCManager;
import net.trustgames.lobby.Lobby;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SpawnNPC implements Listener {

    private final Lobby lobby;

    private final Core core;

    public SpawnNPC(Lobby lobby, Core core) {
        this.lobby = lobby;
        this.core = lobby.getCore();
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event){
        NPCConfig npcConfig = new NPCConfig(lobby);

        NPCManager npcManager = new NPCManager(core);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(npcConfig.getNPCFile());
        Player player = event.getPlayer();

        for (String name :config.getConfigurationSection("npcs").getKeys(false)){
            Location location = config.getLocation("npcs." + name + ".location");
            World world = location.getWorld();
            String texture = config.getString("npcs." + name + ".texture");
            String signature = config.getString("npcs." + name + ".signature");

            ServerPlayer npc = npcManager.create(location, name);
        }
    }
}
