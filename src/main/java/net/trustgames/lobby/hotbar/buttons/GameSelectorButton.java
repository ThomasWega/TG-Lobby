package net.trustgames.lobby.hotbar.buttons;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.trustgames.core.managers.gui.buttons.HotbarGUIButton;
import net.trustgames.core.managers.item.ItemBuilder;
import org.bukkit.Material;

import java.util.List;

public class GameSelectorButton {

    @Getter
    private final HotbarGUIButton button = new HotbarGUIButton()
            .creator(player -> new ItemBuilder(Material.NETHER_STAR)
                    .displayName(Component.text("Start playing", NamedTextColor.YELLOW)
                            .appendSpace()
                            .append(Component.text("(right-click)", NamedTextColor.GRAY))
                    )
                    .lore(List.of(
                                    Component.text("Select a game to play", NamedTextColor.DARK_GRAY),
                                    Component.text("and start playing with", NamedTextColor.DARK_GRAY),
                                    Component.text("oher players righ away!", NamedTextColor.DARK_GRAY)
                            )
                    )
                    .hideFlags()
            )
            .event(event -> event.getWhoClicked().sendMessage(Component.text("TO ADD")))
            .eventHotbar(event -> event.getPlayer().sendMessage(Component.text("TO ADD")));
}
