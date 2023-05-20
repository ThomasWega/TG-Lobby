package net.trustgames.lobby.hotbar;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.trustgames.core.managers.gui.GUIManager;
import net.trustgames.core.managers.gui.buttons.HotbarGUIButton;
import net.trustgames.core.managers.gui.player.PlayerGUI;
import net.trustgames.core.managers.item.ItemBuilder;
import net.trustgames.core.managers.item.SkullBuilder;
import net.trustgames.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.Map;

public class HotbarHandler implements Listener {

    private final GUIManager guiManager;

    public HotbarHandler(Lobby lobby) {
        this.guiManager = lobby.getCore().getGuiManager();
        Bukkit.getPluginManager().registerEvents(this, lobby);
    }

    HashMap<Integer, HotbarGUIButton> buttonMap = new HashMap<>(Map.of(
            1, new HotbarGUIButton()
                    .creator(player -> new SkullBuilder()
                            .owner(player)
                            .displayName(
                                    Component.text("My profile", NamedTextColor.GREEN)
                            )
                            .hideFlags()
                            .build()
                    )
                    .event(event -> event.getWhoClicked().sendMessage(Component.text("TO ADD INV")))
                    .eventHotbar(event -> event.getPlayer().sendMessage(Component.text("TO ADD HOTBAR"))),

            2, new HotbarGUIButton()
                    .creator(player -> new ItemBuilder(Material.COMPASS)
                            .displayName(Component.text("My profile", NamedTextColor.GREEN))
                            .hideFlags()
                            .build()
                    )
                    .event(event -> event.getWhoClicked().sendMessage(Component.text("TO ADD INV")))
                    .eventHotbar(event -> event.getPlayer().sendMessage(Component.text("TO ADD HOTBAR")))
    ));

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        new PlayerGUI(guiManager, player).setContents(buttonMap);
    }
}