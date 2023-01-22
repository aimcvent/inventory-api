package fr.aimcvent.inventory.api;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public interface Item<T extends Plugin>
{
    ItemStack icon();

    void click(Inventory<T> inventory, ClickType clickType);
}
