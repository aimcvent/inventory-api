package fr.aimcvent.inventory.listener;

import fr.aimcvent.inventory.api.Inventories;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

public class ClickListener<T extends Plugin> implements Listener {
    private final Inventories<T> inventories;
    public ClickListener(Inventories<T> inventories) {
        this.inventories = inventories;
    }

    @EventHandler
    protected void on(InventoryClickEvent event) {
        this.inventories.of(event.getClickedInventory())
            .ifPresent(inventory -> {
                inventory.click(event.getClick(), event.getSlot());
                event.setCancelled(true);
            });
    }
}
