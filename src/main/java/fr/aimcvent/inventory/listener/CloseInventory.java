package fr.aimcvent.inventory.listener;

import fr.aimcvent.inventory.api.Inventories;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.Plugin;

public class CloseInventory<T extends Plugin> implements Listener {
    private final Inventories<T> inventories;

    public CloseInventory(Inventories<T> inventories) {
        this.inventories = inventories;
    }

    @EventHandler
    protected void on(InventoryCloseEvent event) {
        this.inventories.close(event.getInventory());
    }
}
