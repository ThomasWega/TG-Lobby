package net.trustgames.lobby.hotbar;

import net.kyori.adventure.text.Component;
import net.trustgames.core.managers.ItemManager;
import net.trustgames.core.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the creation, adding and updating of items
 * in the player's hotbar
 */
public final class HotbarItems {

    static Inventory inventory;

    /**
     * list of hotbar items to add
     *
     * @param player Player to set items to
     */
    public static void addItems(@NotNull Player player) {

        inventory = player.getInventory();

        // item flags to set for every item
        ItemFlag[] itemFlags = new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE};

        // server selector
        ItemStack selectorStack = ItemManager.getSkull(
                "eyJ0aW1lc3RhbXAiOjE1MTgxODUxNjg4OTEsInByb2ZpbGVJZCI6IjIzZjFhNTlmNDY5YjQzZGRiZGI1MzdiZmVjMTA0NzFmIiwicHJvZmlsZU5hbWUiOiIyODA3Iiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83YTRiODgzMmFmYzNjZWE4MzIyNGIxNDQxMGI2NjJlNzA3ZThlNzljNjFmNDY2ZTM2MmEzMGU4MmM3ZGU5In19fQ==",
                "WsGW3iUx8g+xH1NraTlpxgnsp8ClAE40oerBpV+MnPNmBAbZ/WnZwVEI5w9pziuCG75oxuckrO/kyFFHzxivr+OXKoXYwi8jw+8ZRL7v5gappa5nvdbf5kkClNxmxkjDzbN0U01tM91OWIimKwLo5Lel+Zf02vtYw7CF9sM8rowLKAjQzo1Nydb0Hpo2WFsIe4Aa17vyXsHsv4OTDx4Mj1zUvXtFkgTzNCjBuBAfOBaGXp+m6Zo23XJAu010Rf6by0y8OuEm0XHKMY0SXlpE2AepIQDbaMQA2tlViVsDJLAIJJQh0SBILRxC6+8aGdQmC1AG1L7RmtDrIShx6pwF0gGK5BBG0BTtbJ++NtR0sIBk2oNFvm+TtsG/VmIHL6cDiJ7sxvbNJXrr5lp8YJ9Wsgn6P+iJ/qtdSGOfoEQkBmneRZvelbWeUYcpZi8xinoOmgQCDzIWnKZ96T73ArbBZBbeL+6+XGi7lnqv4DMqqfeyzG3YC6SmCzkiBABz5PDxKG0BthnT7Xw0rocBsxHhjWmWC5okskKHZql+Pk1PZnCXWuaC30HxScH11QHy0li4y3J5mclhqISd5/ZbGx/wuOcETQrFKh9M90qObf5VGNxAJovwdIfs3Zh+YCBdYWzsx8UCbLKmewfGWxpat1VgrHmSumE2uojdk81W8lCmyOs="
        );
        ItemMeta selectorMeta = ItemManager.createItemMeta(selectorStack,
                ColorUtils.color("&6Server Selector" + " &7(Use)"), itemFlags);
        selectorStack.setItemMeta(selectorMeta);

        // player profile
        ItemStack profileStack = ItemManager.createItemStack(Material.PLAYER_HEAD, 1);
        ItemMeta profileMeta = ItemManager.createItemMeta(profileStack,
                ColorUtils.color("&aMy Profile" + " &7(Use)"), itemFlags);

        SkullMeta skullProfileMeta = (SkullMeta) profileMeta;
        skullProfileMeta.setOwningPlayer(player);

        // add the items to the inventory
        inventory.setItem(0, selectorStack);
        inventory.setItem(8, profileStack);
    }

    /**
     * update the hide item amount and lore in hotbar
     */
    public static void hidePlayersItem() {

        // hide players item
        ItemStack hideStack = ItemManager.createItemStack(Material.FEATHER, Bukkit.getOnlinePlayers().size());
        ItemMeta hideMeta = hideStack.getItemMeta();
        hideMeta.displayName(Component.text(ChatColor.WHITE + "Hide Players" + ChatColor.GRAY + " (Use)"));

        List<Component> hideLore = new ArrayList<>();
        if (Bukkit.getOnlinePlayers().size() == 1) {
            hideLore.add(Component.text(""));
            hideLore.add(Component.text(ChatColor.DARK_GRAY + "Nobody else is online :("));
        } else {
            hideLore.add(Component.text(""));
            hideLore.add(Component.text(ChatColor.DARK_GRAY + "Hide " + (Bukkit.getOnlinePlayers().size() - 1) + " player(s) by"));
            hideLore.add(Component.text(ChatColor.DARK_GRAY + "clicking with this item"));
        }

        hideMeta.lore(hideLore);
        hideStack.setItemMeta(hideMeta);

        // loop through the online player and set for each one the new amount
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (hideStack.getAmount() <= 64) {
                if (Bukkit.getOnlinePlayers().size() == 1) {
                    hideStack.setAmount(1);
                } else {
                    hideStack.setAmount(Bukkit.getOnlinePlayers().size() - 1);
                }
                onlinePlayer.getInventory().setItem(6, hideStack);
            }
        }
    }

    /**
     * update the player profile item amount and lore in hotbar
     *
     * @param count Player's level
     */
    public static void updateProfileItem(@NotNull Integer count) {
        // TODO finish
    }
}

