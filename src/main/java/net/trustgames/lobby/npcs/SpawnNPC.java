package net.trustgames.lobby.npcs;

import net.minecraft.server.level.EntityPlayer;
import net.trustgames.core.Core;
import net.trustgames.core.managers.NPCManager;
import net.trustgames.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Set;

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

        Set<String> keys = config.getConfigurationSection("npcs").getKeys(false);

        for (String name : keys){
            Location location = config.getLocation("npcs." + name + ".location");
            float yaw = location.getYaw();
            float pitch = location.getPitch();
            String texture = config.getString("npcs." + name + ".texture");
            String signature = config.getString("npcs." + name + ".signature");

            EntityPlayer npc = npcManager.create(location, name);
            npcManager.hideName(npc);
            npcManager.add(npc, player);

            Bukkit.getScheduler().runTaskLater(lobby, () -> {
                npcManager.skin(npc, player, texture, signature);
                npcManager.look(npc, player, yaw, pitch, true);

                Bukkit.getScheduler().runTaskLater(core, () -> npcManager.hideTab(npc, player), 30);

            }, 50);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();

    }
}
