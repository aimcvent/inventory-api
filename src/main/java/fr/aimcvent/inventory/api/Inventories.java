package fr.aimcvent.inventory.api;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

public interface Inventories<T extends Plugin>
{
    Optional<Inventory<T>> of(org.bukkit.inventory.Inventory inventory);

    void open(Player player, Class<? extends Inventory<T>> clazz);

    void close(org.bukkit.inventory.Inventory inventory);
}
