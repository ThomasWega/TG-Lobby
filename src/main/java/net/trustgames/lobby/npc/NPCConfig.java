package net.trustgames.lobby.npc;

import net.trustgames.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class NPCConfig {

    private final Lobby lobby;

    public NPCConfig(Lobby lobby) {
        this.lobby = lobby;
    }

    public void createDefaults() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(getNPCFile());

        // config defaults
        config.addDefault("npcs.npc1.look-at-player", false);
        config.addDefault("npcs.npc1.location", new Location(Bukkit.getWorld("world"), 40d, 78d, 18d, 60, 0));
        config.addDefault("npcs.npc1.texture", "ewogICJ0aW1lc3RhbXAiIDogMTY3NDkxOTkyNDA4NiwKICAicHJvZmlsZUlkIiA6ICIxMzAyMWNlNmEzNGU0ODI3ODhkNTczNTY5MWJmYzU1MSIsCiAgInByb2ZpbGVOYW1lIiA6ICJ2aWJlYm95IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2Y5ZTc1ZjlmOGZkMmYwMzMyNTEwMzU1N2U1OTA1MDA5NzJjZDM2OGFjM2YxOWU0NjgyNjAxMGU5MWZmYzZiM2MiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==");
        config.addDefault("npcs.npc1.signature", "Ea0OC1adIwyf7WQAhS7Uw1SG6PCYKbVq/HhvtibNSUL5yn0HGTNn01GEL3dj2XjOzdpugruZLqc+YJQF8ag6wnJj8WDoraZvIGbP5eJ4Amry+oi7KsAvQQZSPYln4nZv6vDhyItWHxorA8uF6MfN9LevDsyycQJovGgvRolSvXz7qSlUuLdhDB81qBeLBjnkoaDOjYTtOg2pim2gVLTInNLK9u2h0FKud80JwjdsbyajgXPTuiFgRI2328MTTWwZe/73aTG5XhK+18zWss+ucwQYg6NW4bMdGFxB9d7hmi9KyZUAUbcHVb96WZ7/RErjky7ATgszUBNA3xD+XghwPuqOlBjmPgUnPLPYyS7K5YNUujhW4MTZL+UxJ+LYa2eDSekR8uqGw7aqsyR2sCAealmQtIh44Z8tg+d/rqVqb8bdiPaSN/CA9ugO9I+uCnd2M/P39B6UTUq2Wib8lIwb0TJd8HBz6IKgqWtiTKHjYCbwnAx269av1SxMM5UkNygYP0RdOk7/h+QJxCa7TML57Mvqs2dBwHdKFnt3Y9sum4uvrXHAhXpBZ+HeYbOynnUXVbJhI/5mzdK7Y0Qf8OrY7w+5fZ+miq9KXUeuBdIj9AJE8zZJve9yymk0IKc2igINjBbPRTKLoOMkBQrHbKpr5aSOIHRtQYRjU6BmPnVYxwU=");
        config.addDefault("npcs.npc1.holo-text", List.of("&e&lCLICK TO PLAY", "&cSERVER-1", "&77 players"));
        config.addDefault("npcs.npc1.holo-elevate", 0.3);
        config.addDefault("npcs.npc1.glow", "&e");
        config.addDefault("npcs.npc1.equipment.main-hand", "GOLDEN_AXE");
        config.addDefault("npcs.npc1.equipment.off-hand", "FISHING_ROD");
        config.addDefault("npcs.npc1.equipment.head", "AIR");
        config.addDefault("npcs.npc1.equipment.chest", "AIR");
        config.addDefault("npcs.npc1.equipment.legs", "AIR");
        config.addDefault("npcs.npc1.equipment.boots", "AIR");
        config.addDefault("npcs.npc1.action.type", "MESSAGE");
        config.addDefault("npcs.npc1.action.value", List.of(
                "<newline>",
                "<hover:show_text:'<yellow>Click to open changelog</yellow>'><click:open_url:'http://changelog.trustgames.net'><color:#ffd6ba>In the making... |</color> </hover><dark_gray>This feature is coming in the near future</dark_gray>",
                "<newline>"
        ));
        config.addDefault("npcs.npc1.action.cooldown", 2d);

        config.addDefault("npcs.npc2.look-at-player", false);
        config.addDefault("npcs.npc2.location", new Location(Bukkit.getWorld("world"), 40d, 78d, 16d, 90, 0));
        config.addDefault("npcs.npc2.texture", "ewogICJ0aW1lc3RhbXAiIDogMTY3NDkxOTkyNDA4NiwKICAicHJvZmlsZUlkIiA6ICIxMzAyMWNlNmEzNGU0ODI3ODhkNTczNTY5MWJmYzU1MSIsCiAgInByb2ZpbGVOYW1lIiA6ICJ2aWJlYm95IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2Y5ZTc1ZjlmOGZkMmYwMzMyNTEwMzU1N2U1OTA1MDA5NzJjZDM2OGFjM2YxOWU0NjgyNjAxMGU5MWZmYzZiM2MiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==");
        config.addDefault("npcs.npc2.signature", "Ea0OC1adIwyf7WQAhS7Uw1SG6PCYKbVq/HhvtibNSUL5yn0HGTNn01GEL3dj2XjOzdpugruZLqc+YJQF8ag6wnJj8WDoraZvIGbP5eJ4Amry+oi7KsAvQQZSPYln4nZv6vDhyItWHxorA8uF6MfN9LevDsyycQJovGgvRolSvXz7qSlUuLdhDB81qBeLBjnkoaDOjYTtOg2pim2gVLTInNLK9u2h0FKud80JwjdsbyajgXPTuiFgRI2328MTTWwZe/73aTG5XhK+18zWss+ucwQYg6NW4bMdGFxB9d7hmi9KyZUAUbcHVb96WZ7/RErjky7ATgszUBNA3xD+XghwPuqOlBjmPgUnPLPYyS7K5YNUujhW4MTZL+UxJ+LYa2eDSekR8uqGw7aqsyR2sCAealmQtIh44Z8tg+d/rqVqb8bdiPaSN/CA9ugO9I+uCnd2M/P39B6UTUq2Wib8lIwb0TJd8HBz6IKgqWtiTKHjYCbwnAx269av1SxMM5UkNygYP0RdOk7/h+QJxCa7TML57Mvqs2dBwHdKFnt3Y9sum4uvrXHAhXpBZ+HeYbOynnUXVbJhI/5mzdK7Y0Qf8OrY7w+5fZ+miq9KXUeuBdIj9AJE8zZJve9yymk0IKc2igINjBbPRTKLoOMkBQrHbKpr5aSOIHRtQYRjU6BmPnVYxwU=");
        config.addDefault("npcs.npc2.holo-text", List.of("&e&lCLICK TO PLAY", "&cSERVER-1", "&77 players"));
        config.addDefault("npcs.npc2.holo-elevate", 0.3);
        config.addDefault("npcs.npc2.glow", "&e");
        config.addDefault("npcs.npc2.equipment.main-hand", "GOLDEN_AXE");
        config.addDefault("npcs.npc2.equipment.off-hand", "FISHING_ROD");
        config.addDefault("npcs.npc2.equipment.head", "AIR");
        config.addDefault("npcs.npc2.equipment.chest", "AIR");
        config.addDefault("npcs.npc2.equipment.legs", "AIR");
        config.addDefault("npcs.npc2.equipment.boots", "AIR");
        config.addDefault("npcs.npc2.action.type", "MESSAGE");
        config.addDefault("npcs.npc2.action.value", List.of(
                "<newline>",
                "<hover:show_text:'<yellow>Click to open changelog</yellow>'><click:open_url:'http://changelog.trustgames.net'><color:#ffd6ba>In the making... |</color> </hover><dark_gray>This feature is coming in the near future</dark_gray>",
                "<newline>"
        ));
        config.addDefault("npcs.npc2.action.cooldown", 2d);

        config.addDefault("npcs.npc3.look-at-player", false);
        config.addDefault("npcs.npc3.location", new Location(Bukkit.getWorld("world"), 40d, 78d, 14d, 120, 0));
        config.addDefault("npcs.npc3.texture", "ewogICJ0aW1lc3RhbXAiIDogMTY3NDkxOTkyNDA4NiwKICAicHJvZmlsZUlkIiA6ICIxMzAyMWNlNmEzNGU0ODI3ODhkNTczNTY5MWJmYzU1MSIsCiAgInByb2ZpbGVOYW1lIiA6ICJ2aWJlYm95IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2Y5ZTc1ZjlmOGZkMmYwMzMyNTEwMzU1N2U1OTA1MDA5NzJjZDM2OGFjM2YxOWU0NjgyNjAxMGU5MWZmYzZiM2MiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==");
        config.addDefault("npcs.npc3.signature", "Ea0OC1adIwyf7WQAhS7Uw1SG6PCYKbVq/HhvtibNSUL5yn0HGTNn01GEL3dj2XjOzdpugruZLqc+YJQF8ag6wnJj8WDoraZvIGbP5eJ4Amry+oi7KsAvQQZSPYln4nZv6vDhyItWHxorA8uF6MfN9LevDsyycQJovGgvRolSvXz7qSlUuLdhDB81qBeLBjnkoaDOjYTtOg2pim2gVLTInNLK9u2h0FKud80JwjdsbyajgXPTuiFgRI2328MTTWwZe/73aTG5XhK+18zWss+ucwQYg6NW4bMdGFxB9d7hmi9KyZUAUbcHVb96WZ7/RErjky7ATgszUBNA3xD+XghwPuqOlBjmPgUnPLPYyS7K5YNUujhW4MTZL+UxJ+LYa2eDSekR8uqGw7aqsyR2sCAealmQtIh44Z8tg+d/rqVqb8bdiPaSN/CA9ugO9I+uCnd2M/P39B6UTUq2Wib8lIwb0TJd8HBz6IKgqWtiTKHjYCbwnAx269av1SxMM5UkNygYP0RdOk7/h+QJxCa7TML57Mvqs2dBwHdKFnt3Y9sum4uvrXHAhXpBZ+HeYbOynnUXVbJhI/5mzdK7Y0Qf8OrY7w+5fZ+miq9KXUeuBdIj9AJE8zZJve9yymk0IKc2igINjBbPRTKLoOMkBQrHbKpr5aSOIHRtQYRjU6BmPnVYxwU=");
        config.addDefault("npcs.npc3.holo-text", List.of("&e&lCLICK TO PLAY", "&cSERVER-1", "&77 players"));
        config.addDefault("npcs.npc3.holo-elevate", 0.3);
        config.addDefault("npcs.npc3.glow", "&e");
        config.addDefault("npcs.npc3.equipment.main-hand", "GOLDEN_AXE");
        config.addDefault("npcs.npc3.equipment.off-hand", "FISHING_ROD");
        config.addDefault("npcs.npc3.equipment.head", "AIR");
        config.addDefault("npcs.npc3.equipment.chest", "AIR");
        config.addDefault("npcs.npc3.equipment.legs", "AIR");
        config.addDefault("npcs.npc3.equipment.boots", "AIR");
        config.addDefault("npcs.npc3.action.type", "MESSAGE");
        config.addDefault("npcs.npc3.action.value", List.of(
                "<newline>",
                "<hover:show_text:'<yellow>Click to open changelog</yellow>'><click:open_url:'http://changelog.trustgames.net'><color:#ffd6ba>In the making... |</color> </hover><dark_gray>This feature is coming in the near future</dark_gray>",
                "<newline>"
        ));
        config.addDefault("npcs.npc3.action.cooldown", 2d);


        try {
            config.options().copyDefaults(true);
            config.save(getNPCFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public File getNPCFile() {
        return new File(lobby.getDataFolder(), "npcs.yml");
    }
}
