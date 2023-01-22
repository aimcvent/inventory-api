package fr.aimcvent.inventory;

import fr.aimcvent.inventory.api.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPaginationInventory<T extends Plugin> implements PaginationInventory<T> {
    protected final List<Inventory<T>> inventoryList = new ArrayList<>();
    protected final Inventories<T> inventories;
    protected final Player player;
    protected final T plugin;
    protected Inventory<T> activeInventory;

    protected AbstractPaginationInventory(T plugin, Inventories<T> inventories, Player player) {
        this.plugin = plugin;
        this.inventories = inventories;
        this.player = player;
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
        return this.activeInventory.bukkit();
    }

    @Override
    public void add(int slot, Item<T> item) {
        if (this.activeInventory != null) {
            this.activeInventory.add(slot, item);
        }
    }

    @Override
    public void remove(int slot) {
        if (this.activeInventory != null) {
            this.activeInventory.remove(slot);
        }
    }

    @Override
    public void update() {
        this.inventoryList.forEach(Inventory::update);
    }

    @Override
    public void reload() {
        this.inventoryList.clear();
        this.activeInventory = null;
    }

    @Override
    public void click(ClickType clickType, int slot) {
        if (this.activeInventory != null) {
            this.activeInventory.click(clickType, slot);
        }
    }

    protected final void addPage(Inventory<T> inventory) {
        this.inventoryList.add(inventory);
        if (this.inventoryList.size() == 1) {
            this.activeInventory = inventory;
        } else {
            PaginateItem<T> item = this.paginateItem(false);
            inventory.add(item.slot(inventory.bukkit()), item);
            final Inventory<T> nextInventory = this.inventoryList.get(this.inventoryList.size() - 2);
            item = this.paginateItem(true);
            nextInventory.add(item.slot(nextInventory.bukkit()), item);
        }
    }

    @Override
    public void next() {
        boolean next = false;
        for (Inventory<T> inventory : this.inventoryList) {
            if (this.activeInventory == null || next) {
                this.changeActiveInventory(inventory);
                return;
            }
            if (this.activeInventory.equals(inventory)) {
                next = true;
            }
        }
    }

    protected PaginateItem<T> paginateItem(boolean next) {
        return new ImplPaginateItem<>(this, next);
    }

    @Override
    public void previous() {
        Inventory<T> previous = null;
        for (Inventory<T> inventory : this.inventoryList) {
            if (this.activeInventory == null || this.activeInventory.equals(inventory)) {
                this.changeActiveInventory(previous != null ? previous : inventory);
                return;
            }
            previous = inventory;
        }
    }

    protected void changeActiveInventory(Inventory<T> inventory) {
        this.inventories.close(this.activeInventory.bukkit());
        this.activeInventory = inventory;
        ((ImplInventories<T>) this.inventories).open(this);
        this.player.openInventory(this.activeInventory.bukkit());
    }
}
