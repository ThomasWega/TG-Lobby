package net.trustgames.lobby.hotbar;

import net.trustgames.core.gui.GUIManager;
import net.trustgames.core.gui.type.PlayerGUI;
import net.trustgames.lobby.Lobby;
import net.trustgames.lobby.hotbar.buttons.GameSelectorButton;
import net.trustgames.lobby.hotbar.buttons.HidePlayersButton;
import net.trustgames.lobby.hotbar.buttons.ProfileButton;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class HotbarHandler implements Listener {
    private final Lobby lobby;
    private final GUIManager guiManager;
    private final HidePlayersButton hidePlayersButton;
    private final HashMap<Player, PlayerGUI> guis = new HashMap<>();

    public HotbarHandler(Lobby lobby) {
        this.lobby = lobby;
        this.guiManager = lobby.getCore().getGuiManager();
        this.hidePlayersButton = new HidePlayersButton(lobby);

        Bukkit.getPluginManager().registerEvents(this, lobby);
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerGUI playerGUI = new PlayerGUI(guiManager, player);
        playerGUI.setContents(Map.of(
                0, new GameSelectorButton().getButton(),
                4, hidePlayersButton.getButton(playerGUI),
                8, new ProfileButton().getButton()
        ));
        updateForAll();
        guis.put(player, playerGUI);
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        guis.remove(player);
        hidePlayersButton.removePlayerStatus(player);
        updateForAll();
    }

    private void updateForAll() {
        Bukkit.getScheduler().runTaskLater(lobby, () -> {
            hidePlayersButton.updateHiddenPlayers();
            guis.values().forEach(pGUI -> pGUI.setButton(4, hidePlayersButton.getButton(pGUI)));
        }, 10L);
    }
}