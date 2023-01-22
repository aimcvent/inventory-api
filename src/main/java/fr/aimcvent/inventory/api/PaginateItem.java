package fr.aimcvent.inventory.api;

import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public interface PaginateItem<T extends Plugin> extends Item<T> {
    int slot(Inventory inventory);
}
