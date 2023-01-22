package fr.aimcvent.inventory;

import fr.aimcvent.inventory.api.Inventories;
import fr.aimcvent.inventory.api.Inventory;
import fr.aimcvent.inventory.api.PaginationInventory;
import fr.aimcvent.inventory.listener.ClickListener;
import fr.aimcvent.inventory.listener.CloseInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

public class ImplInventories<T extends Plugin> implements Inventories<T> {

    public static <T extends Plugin> Inventories<T> create(T plugin) {
        final Inventories<T> inventories = new ImplInventories<>(plugin);
        Bukkit.getPluginManager().registerEvents(new ClickListener<>(inventories), plugin);
        Bukkit.getPluginManager().registerEvents(new CloseInventory<>(inventories), plugin);
        return inventories;
    }

    private final Map<org.bukkit.inventory.Inventory, Inventory<T>> inventoryMap = new HashMap<>();
    private final T plugin;

    private ImplInventories(T plugin) {
        this.plugin = plugin;
    }

    @Override
    public Optional<Inventory<T>> of(org.bukkit.inventory.Inventory inventory) {
        return Optional.ofNullable(this.inventoryMap.get(inventory));
    }

    @Override
    public void open(Player player, Class<? extends Inventory<T>> clazz) {
        try {
            final Inventory<T> inventory = ((Constructor<Inventory<T>>) clazz.getConstructors()[0])
                    .newInstance(this.plugin, this, player);
            inventory.reload();
            this.inventoryMap.put(inventory.bukkit(), inventory);
            player.openInventory(inventory.bukkit());
        } catch (Exception e) {
            this.plugin.getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void open(PaginationInventory<T> inventory) {
        this.inventoryMap.put(inventory.bukkit(), inventory);
    }

    @Override
    public void close(org.bukkit.inventory.Inventory inventory) {
        this.inventoryMap.remove(inventory);
    }
}
