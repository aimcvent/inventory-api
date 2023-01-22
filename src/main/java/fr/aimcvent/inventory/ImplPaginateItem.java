package fr.aimcvent.inventory;

import fr.aimcvent.inventory.api.Inventory;
import fr.aimcvent.inventory.api.PaginateItem;
import fr.aimcvent.inventory.api.PaginationInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class ImplPaginateItem<T extends Plugin> implements PaginateItem<T> {
    private final PaginationInventory<T> paginationInventory;
    private final boolean next;

    public ImplPaginateItem(PaginationInventory<T> paginationInventory, boolean next) {
        this.paginationInventory = paginationInventory;
        this.next = next;
    }

    @Override
    public int slot(org.bukkit.inventory.Inventory inventory) {
        return this.next ? (inventory.getSize() - 1) : (inventory.getSize() - 9);
    }

    @Override
    public ItemStack icon() {
        final ItemStack itemStack = new ItemStack(Material.STONE_BUTTON);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + (this.next ? "Next" : "Previous"));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public void click(Inventory<T> inventory, ClickType clickType) {
        if (this.next) {
            this.paginationInventory.next();
            return;
        }
        this.paginationInventory.previous();
    }
}
