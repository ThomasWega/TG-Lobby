package net.trustgames.lobby.npc;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.trustgames.core.Core;
import net.trustgames.core.managers.packets.HoloManager;
import net.trustgames.core.managers.packets.NPCManager;
import net.trustgames.core.utils.ColorUtils;
import net.trustgames.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

/**
 * Class used to spawn all the npcs and holograms
 */
public final class NPCHandler implements Listener {

    private final Lobby lobby;

    private final Core core;

    private final NPCManager npcManager;
    private final YamlConfiguration config;

    // stores list of all the npcs for given player
    private final HashMap<String, List<ServerPlayer>> npcs = new HashMap<>();
    // stores list of all the npcs that should be looking at the player
    private final HashMap<ServerPlayer, Location> npcsLookAtPlayer = new HashMap<>();

    public NPCHandler(Lobby lobby) {
        this.lobby = lobby;
        this.core = lobby.getCore();
        this.npcManager = new NPCManager(core);
        this.config = YamlConfiguration.loadConfiguration(new File(lobby.getDataFolder(), "npcs.yml"));
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        spawn(player);
        setData(player);
        lookAtPlayer(player);
        hide(player);

        npcManager.interact(npcs.get(player.getName()), config);
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        npcs.remove(player.getName());
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        for (ServerPlayer npc : npcsLookAtPlayer.keySet()) {
            npcManager.lookAtPlayer(npc, player, npcsLookAtPlayer.get(npc));
        }
    }

    /**
     * Resolve which NPCs should be looking at the player and put them in a list
     *
     * @param player Player that the NPC will be facing
     */
    private void lookAtPlayer(@NotNull Player player) {
        if (!player.isOnline()) return;

        for (ServerPlayer npc : npcs.get(player.getName())) {
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
    private void spawn(@NotNull Player player) {
        if (!player.isOnline()) return;

        Set<String> keys = Objects.requireNonNull(config.getConfigurationSection("npcs")).getKeys(false);
        List<ServerPlayer> playerNpcs = new ArrayList<>();

        for (String name : keys) {
            Location location = config.getLocation("npcs." + name + ".location");
            List<String> holoText = config.getStringList("npcs." + name + ".holo-text");
            double elevateY = config.getDouble("npcs." + name + ".holo-elevate");

            assert location != null;
            ServerPlayer npc = npcManager.create(location, name);
            npcManager.add(npc, player);

            HoloManager.spawn(player, location.clone().add(0, elevateY, 0), holoText);

            playerNpcs.add(npc);
        }
        npcs.put(player.getName(), playerNpcs);
    }

    /**
     * Set the skin, equipment and look location for all the NPCs
     *
     * @param player Player to set the data for NPCs to
     */
    private void setData(@NotNull Player player) {
        Bukkit.getScheduler().runTaskLater(lobby, () -> {
            if (!player.isOnline()) return;

            for (ServerPlayer npc : npcs.get(player.getName())) {
                Location location = config.getLocation("npcs." + npc.displayName + ".location");
                assert location != null;
                float yaw = location.getYaw();
                float pitch = location.getPitch();
                String texture = config.getString("npcs." + npc.displayName + ".texture");
                String signature = config.getString("npcs." + npc.displayName + ".signature");
                String glow = config.getString("npcs." + npc.displayName + ".glow");

                if (glow != null && !glow.equals("false")) {
                    TextColor color = ColorUtils.color(glow).color();
                    if (color != null)
                        npcManager.glow(npc, color);
                } else {
                    npcManager.hideName(npc);
                }

                if (texture != null && signature != null)
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
    private void hide(@NotNull Player player) {
        Bukkit.getScheduler().runTaskLater(core, () -> {
            if (!player.isOnline()) return;
            for (ServerPlayer npc : npcs.get(player.getName())) {
                npcManager.hideTab(npc, player);
            }
        }, 70);
    }
}
