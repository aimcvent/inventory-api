package fr.aimcvent.inventory;

import fr.aimcvent.inventory.api.Inventories;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class DefaultPageInventory<T extends Plugin> extends AbstractInventory<T> {
    public DefaultPageInventory(T plugin, Inventories<T> inventories, Player player, String title, int lines) {
        super(plugin, inventories, player, title, lines);
    }

    @Override
    public void reload() {
        this.clear();
    }
}
