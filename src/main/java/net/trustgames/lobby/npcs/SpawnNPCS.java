package net.trustgames.lobby.npcs;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import net.minecraft.server.level.EntityPlayer;
import net.trustgames.core.Core;
import net.trustgames.core.cache.EntityCache;
import net.trustgames.core.managers.HoloManager;
import net.trustgames.core.managers.NPCManager;
import net.trustgames.core.utils.ColorUtils;
import net.trustgames.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Class used to spawn all the npcs and holograms
 */
public class SpawnNPCS implements Listener {

    private final Lobby lobby;

    private final Core core;

    private final NPCManager npcManager;
    private final HoloManager holoManager;

    private final NPCConfig npcConfig;


    public SpawnNPCS(Lobby lobby, Core core) {
        this.lobby = lobby;
        this.core = lobby.getCore();
        this.npcManager = new NPCManager(core);
        this.holoManager = new HoloManager();
        npcConfig = new NPCConfig(lobby);
    }

    // stores list of all the npcs for given player
    private final HashMap<UUID, List<EntityPlayer>> npcs = new HashMap<>();

    // stores list of all the npcs that should be looking at the player
    private final HashMap<EntityPlayer, Location> npcsLookAtPlayer = new HashMap<>();

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        UUID uuid = EntityCache.getUUID(player);

        spawn(player);
        setData(player);
        hide(player);
        lookAtPlayer(player);

        npcManager.interact(npcs.get(uuid), YamlConfiguration.loadConfiguration(npcConfig.getNPCFile()));
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        UUID uuid = EntityCache.getUUID(player);
        npcs.remove(uuid);
    }

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        for (EntityPlayer npc : npcsLookAtPlayer.keySet()){
            npcManager.lookAtPlayer(npc, player, npcsLookAtPlayer.get(npc));
        }
    }

    /**
     * Resolve which NPCs should be looking at the player and put them in a list
     *
     * @param player Player that the NPC will be facing
     */
    private void lookAtPlayer(Player player){
        YamlConfiguration config = YamlConfiguration.loadConfiguration(npcConfig.getNPCFile());
        UUID uuid = EntityCache.getUUID(player);

        for (EntityPlayer npc : npcs.get(uuid)){
            boolean lookAtPlayer = config.getBoolean("npcs." + npc.displayName + ".look-at-player");
            if (lookAtPlayer) {
                Location location = config.getLocation("npcs." + npc.displayName + ".location");
                npcsLookAtPlayer.put(npc, location);
            }
        }
    }

    /**
     * Spawn the NPCs from the config for the given player
     *
     * @param player Player to spawn the NPCS for
     */
    private void spawn(Player player){
        YamlConfiguration config = YamlConfiguration.loadConfiguration(npcConfig.getNPCFile());
        UUID uuid = EntityCache.getUUID(player);
        Set<String> keys = Objects.requireNonNull(config.getConfigurationSection("npcs")).getKeys(false);
        List<EntityPlayer> playerNpcs = new ArrayList<>();

        for (String name : keys){
            Location location = config.getLocation("npcs." + name + ".location");
            List<String> holoText = config.getStringList("npcs." + name + ".holo-text");
            double elevateY = config.getDouble("npcs." + name + ".holo-elevate");

            EntityPlayer npc = npcManager.create(location, name);
            npcManager.hideName(npc);
            npcManager.add(npc, player);

            assert location != null;
            holoManager.spawn(player, location.clone().add(0, elevateY, 0), holoText);

            playerNpcs.add(npc);
        }
        npcs.put(uuid, playerNpcs);
    }

    /**
     * Set the skin, equipment and look location for all the NPCs
     *
     * @param player Player to set the data for NPCs to
     */
    private void setData(Player player){
        Bukkit.getScheduler().runTaskLater(lobby, () -> {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(npcConfig.getNPCFile());
            UUID uuid = EntityCache.getUUID(player);
            for(EntityPlayer npc : npcs.get(uuid)) {
                Location location = config.getLocation("npcs." + npc.displayName + ".location");
                assert location != null;
                float yaw = location.getYaw();
                float pitch = location.getPitch();
                String texture = config.getString("npcs." + npc.displayName + ".texture");
                String signature = config.getString("npcs." + npc.displayName + ".signature");
                String glow = config.getString("npcs." + npc.displayName + ".glow");

                if (glow != null && glow.equals("false"))
                    npcManager.glow(npc, ColorUtils.color(glow).color());
                npcManager.skin(npc, player, texture, signature);

                ItemStack mainHand = new ItemStack(Material.valueOf(config.getString("npcs." + npc.displayName + ".equipment.main-hand")));
                ItemStack offHand = new ItemStack(Material.valueOf(config.getString("npcs." + npc.displayName + ".equipment.off-hand")));
                ItemStack head = new ItemStack(Material.valueOf(config.getString("npcs." + npc.displayName + ".equipment.head")));
                ItemStack chest = new ItemStack(Material.valueOf(config.getString("npcs." + npc.displayName + ".equipment.chest")));
                ItemStack legs = new ItemStack(Material.valueOf(config.getString("npcs." + npc.displayName + ".equipment.legs")));
                ItemStack boots = new ItemStack(Material.valueOf(config.getString("npcs." + npc.displayName + ".equipment.boots")));

                List<Pair<EnumWrappers.ItemSlot, ItemStack>> equipments = new ArrayList<>();
                equipments.add(new Pair<>(EnumWrappers.ItemSlot.MAINHAND, mainHand));
                equipments.add(new Pair<>(EnumWrappers.ItemSlot.OFFHAND, offHand));
                equipments.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, head));
                equipments.add(new Pair<>(EnumWrappers.ItemSlot.CHEST, chest));
                equipments.add(new Pair<>(EnumWrappers.ItemSlot.LEGS, legs));
                equipments.add(new Pair<>(EnumWrappers.ItemSlot.FEET, boots));

                npcManager.equipment(npc, player, equipments);

                npcManager.lookAtPosition(npc, player, yaw, pitch, true);
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
            UUID uuid = EntityCache.getUUID(player);
            for (EntityPlayer npc : npcs.get(uuid)) {
                npcManager.hideTab(npc, player);
            }
        }, 70);
    }
}
