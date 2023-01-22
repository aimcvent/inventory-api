package fr.aimcvent.inventory.api;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.plugin.Plugin;

public interface Inventory<T extends Plugin>
{
    T plugin();

    Player player();

    org.bukkit.inventory.Inventory bukkit();

    void add(int slot, Item<T> item);

    void remove(int slot);

    void update();

    void reload();

    void click(ClickType clickType, int slot);
}
