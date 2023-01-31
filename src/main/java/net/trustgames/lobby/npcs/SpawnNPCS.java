package net.trustgames.lobby.npcs;

import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.trustgames.core.Core;
import net.trustgames.core.managers.HoloManager;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Class used to spawn all the npcs and holograms
 */
public class SpawnNPCS implements Listener {

    private final Lobby lobby;

    private final Core core;

    NPCConfig npcConfig;
    NPCManager npcManager;
    HoloManager holoManager;
    YamlConfiguration config;

    public SpawnNPCS(Lobby lobby, Core core) {
        this.lobby = lobby;
        this.core = lobby.getCore();
        this.npcConfig = new NPCConfig(lobby);
        this.npcManager = new NPCManager(core);
        this.holoManager = new HoloManager();
        this.config = YamlConfiguration.loadConfiguration(npcConfig.getNPCFile());
    }

    List<EntityPlayer> npcs = new ArrayList<>();
    List<EntityArmorStand> armorStands = new ArrayList<>();

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        spawn(player);
        setData(player);
        hide(player);
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();

        for (EntityPlayer npc : npcs){
            npcManager.remove(npc, player);
        }
        for (EntityArmorStand armorStand : armorStands){
            holoManager.remove(armorStand, player);
        }
    }

    /**
     * Spawn the NPCs from the config for the given player
     *
     * @param player Player to spawn the NPCS for
     */
    private void spawn(Player player){
        Set<String> keys = config.getConfigurationSection("npcs").getKeys(false);

        for (String name : keys){
            Location location = config.getLocation("npcs." + name + ".location");
            List<String> holoText = config.getStringList("npcs." + name + ".holo-text");
            double elevateY = config.getDouble("npcs." + name + ".holo-elevate");

            EntityPlayer npc = npcManager.create(location, name);
            npcManager.hideName(npc);
            npcManager.add(npc, player);

            armorStands = holoManager.spawn(player, location.add(0, elevateY, 0), holoText);

            npcs.add(npc);
        }
    }

    /**
     * Set the skin and look location for all the NPCs
     *
     * @param player Player to set the data for NPCs to
     */
    private void setData(Player player){
        Bukkit.getScheduler().runTaskLater(lobby, () -> {
            for(EntityPlayer npc : npcs) {
                Location location = config.getLocation("npcs." + npc.displayName + ".location");
                float yaw = location.getYaw();
                float pitch = location.getPitch();
                String texture = config.getString("npcs." + npc.displayName + ".texture");
                String signature = config.getString("npcs." + npc.displayName + ".signature");

                npcManager.skin(npc, player, texture, signature);
                npcManager.look(npc, player, yaw, pitch, true);
            }
        }, 50);
    }

    /**
     * Hide the all the NPCs from the tablist for the given player.
     *
     * @param player Player to hide the NPCs for
     */
    private void hide(Player player){
        Bukkit.getScheduler().runTaskLater(core, () -> {
            for (EntityPlayer npc : npcs) {
                npcManager.hideTab(npc, player);
            }
        }, 70);
    }
}
