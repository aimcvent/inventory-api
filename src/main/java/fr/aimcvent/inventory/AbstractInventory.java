package fr.aimcvent.inventory;

import fr.aimcvent.inventory.api.Inventories;
import fr.aimcvent.inventory.api.Inventory;
import fr.aimcvent.inventory.api.Item;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractInventory<T extends Plugin> implements Inventory<T> {
    protected final Map<Integer, Item<T>> itemMap = new HashMap<>();
    protected final org.bukkit.inventory.Inventory inventory;
    protected final Inventories<T> inventories;
    protected final T plugin;
    protected final Player player;

    protected AbstractInventory(T plugin, Inventories<T> inventories, Player player, String title, int lines) {
        this.plugin = plugin;
        this.inventories = inventories;
        this.player = player;
        this.inventory = Bukkit.createInventory(null, lines * 9, title);
    }

    @Override
    public T plugin() {
        return this.plugin;
    }

    @Override
    public Player player() {
        return this.player;
    }

    @Override
    public org.bukkit.inventory.Inventory bukkit() {
        return this.inventory;
    }

    @Override
    public void add(int slot, Item<T> item) {
        this.itemMap.put(slot, item);
        this.inventory.setItem(slot, item.icon());
    }

    @Override
    public void remove(int slot) {
        this.itemMap.remove(slot);
        this.inventory.setItem(slot, new ItemStack(Material.AIR));
    }

    @Override
    public void update() {
        this.inventory.clear();
        this.itemMap.forEach((slot, item) -> this.inventory.setItem(slot, item.icon()));
    }

    protected void clear() {
        this.itemMap.clear();
        this.inventory.clear();
    }

    @Override
    public void click(ClickType clickType, int slot) {
        final Item<T> item = this.itemMap.get(slot);
        if (item != null) {
            item.click(this, clickType);
        }
    }
}
