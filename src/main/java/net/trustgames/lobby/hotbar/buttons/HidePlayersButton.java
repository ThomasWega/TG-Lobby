package net.trustgames.lobby.hotbar.buttons;

import com.destroystokyo.paper.ParticleBuilder;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.trustgames.core.managers.gui.buttons.HotbarGUIButton;
import net.trustgames.core.managers.gui.type.PlayerGUI;
import net.trustgames.core.managers.item.ItemBuilder;
import net.trustgames.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class HidePlayersButton {
    private final Lobby lobby;
    private final HashMap<Player, HideStatus> hideStatusMap = new HashMap<>();
    private final ParticleBuilder particleBuilder = new ParticleBuilder(Particle.SPELL)
            .count(10)
            .offset(0.5d, 0.5d, 0.5d);

    private final Sound sound = Sound.sound(org.bukkit.Sound.BLOCK_NOTE_BLOCK_HAT.key(), Sound.Source.BLOCK, 1f, 1f);

    public HidePlayersButton(@NotNull Lobby lobby) {
        this.lobby = lobby;
    }

    /**
     * Resolve what button to get based on the current players hide item status
     *
     * @param playerGUI Player's gui Object
     * @return Either Show or Hide Button
     */
    public HotbarGUIButton getButton(PlayerGUI playerGUI) {
        HideStatus hideStatus = hideStatusMap.getOrDefault(playerGUI.getPlayer(), HideStatus.SHOW);
        if (hideStatus == HideStatus.HIDE) {
            return getShowButton(playerGUI);
        } else if (hideStatus == HideStatus.SHOW) {
            return getHideButton(playerGUI);
        }
        return getHideButton(playerGUI);
    }

    public HotbarGUIButton getHideButton(PlayerGUI playerGUI) {
        return new HotbarGUIButton()
                .creator(player -> new ItemBuilder(Material.GRAY_DYE)
                        .displayName(Component.text("Hide players", NamedTextColor.RED)
                                .appendSpace()
                                .append(Component.text("(right-click)", NamedTextColor.GRAY))
                        )
                        .lore(List.of(
                                        Component.text("Hide all players on this server.", NamedTextColor.DARK_GRAY),
                                        Component.text("You won't be able to see", NamedTextColor.DARK_GRAY),
                                        Component.text("these players until re-enabled.", NamedTextColor.DARK_GRAY)
                                )
                        )
                        .hideFlags())
                .event(event -> hidePlayers(playerGUI))
                .eventHotbar(event -> Bukkit.getScheduler().runTaskLater(lobby, () -> hidePlayers(playerGUI), 1L));
    }

    public HotbarGUIButton getShowButton(PlayerGUI playerGUI) {
        return new HotbarGUIButton()
                .creator(player -> new ItemBuilder(Material.LIME_DYE)
                        .amount(resolveItemAmount())
                        .displayName(Component.text("Show players", NamedTextColor.GREEN)
                                .appendSpace()
                                .append(Component.text("(right-click)", NamedTextColor.GRAY))
                        )
                        .lore(List.of(
                                        Component.text("You are currently have", NamedTextColor.DARK_GRAY),
                                        Component.text((Bukkit.getOnlinePlayers().size() - 1) + " players hidden.", NamedTextColor.DARK_GRAY),
                                        Component.text("Click this item to be", NamedTextColor.DARK_GRAY),
                                        Component.text("able to see them again.", NamedTextColor.DARK_GRAY)
                                )
                        )
                        .hideFlags())
                .event(event -> showPlayers(playerGUI))
                .eventHotbar(event -> Bukkit.getScheduler().runTaskLater(lobby, () -> showPlayers(playerGUI), 1L));
    }

    /**
     * Go through the list of online players and
     * show all players for the given gui owner
     *
     * @param playerGUI Player's gui Object
     */
    private void showPlayers(PlayerGUI playerGUI) {
        Player invoker = playerGUI.getPlayer();
        invoker.sendMessage(Messages.SHOW.getFormatted());
        Bukkit.getOnlinePlayers().stream()
                .filter(p -> !p.equals(invoker))
                .forEach(p -> invoker.showPlayer(lobby, p));
        hideStatusMap.put(invoker, HideStatus.SHOW);
        playerGUI.setButton(4, getHideButton(playerGUI));
        showDecorations(invoker);
    }

    /**
     * Go through the list of online players and
     * hide all players for the given gui owner
     *
     * @param playerGUI Player's gui Object
     */
    private void hidePlayers(PlayerGUI playerGUI) {
        Player invoker = playerGUI.getPlayer();
        invoker.sendMessage(Messages.HIDE.getFormatted());
        Bukkit.getOnlinePlayers().stream()
                .filter(p -> !p.equals(invoker))
                .forEach(p -> invoker.hidePlayer(lobby, p));
        hideStatusMap.put(invoker, HideStatus.HIDE);
        playerGUI.setButton(4, getShowButton(playerGUI));
        showDecorations(invoker);
    }

    /**
     * Play particle around all players being hidden/shown
     * and play sound for the invoker
     *
     * @param invoker Player who used the hide item
     */
    private void showDecorations(Player invoker) {
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> !player.equals(invoker))
                .forEach(player -> particleBuilder.location(player.getLocation())
                        .receivers(invoker)
                        .spawn());
        invoker.playSound(sound);
    }

    /**
     * Go through the list of players who have hidden players
     * and hide all online players again
     */
    public void updateHiddenPlayers() {
        hideStatusMap.entrySet().stream()
                .filter(entry -> entry.getValue() == HideStatus.HIDE)
                .forEach(entry -> {
                    Player player = entry.getKey();
                    Bukkit.getOnlinePlayers().stream()
                            .filter(p -> !p.equals(player))
                            .forEach(p -> player.hidePlayer(lobby, p));
                });
    }

    /**
     * When 1 or 2 players are online, item amount needs to be one,
     * otherwise if more player are online, the amount can be ONLINE_PLAYERS - 1
     */
    public int resolveItemAmount() {
        int amount = Bukkit.getOnlinePlayers().size();
        if (amount <= 2) {
            amount = 1;
        } else {
            amount--;
        }
        return amount;
    }

    public void removePlayerStatus(Player player) {
        hideStatusMap.remove(player);
    }

    private enum HideStatus {
        HIDE, SHOW
    }

    private enum Messages {
        PREFIX("<color:#0fd934>Item | "),
        HIDE(PREFIX.value + "<dark_gray>From now on, all online players will be hidden"),
        SHOW(PREFIX.value + "<dark_gray>From now on, you will be able to see all online players again");

        private final String value;

        Messages(String value) {
            this.value = value;
        }

        public Component getFormatted() {
            return MiniMessage.miniMessage().deserialize(value);
        }
    }
}
