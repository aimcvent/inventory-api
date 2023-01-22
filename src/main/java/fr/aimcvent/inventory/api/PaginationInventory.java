package fr.aimcvent.inventory.api;

import org.bukkit.plugin.Plugin;

public interface PaginationInventory<T extends Plugin> extends Inventory<T> {
    void next();

    void previous();
}
