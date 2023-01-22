# Inventory API [MINECRAFT 1.8+]

Cette api a été conçu pour réalisé des projets de mini-jeux utilisant des inventaires.

---

## Implémentation Gradle
```gradle
repositories {
  mavenCentral()
  maven { url "https://jitpack.io" }
}

dependencies {
  implementation 'com.github.aimcvent:inventory-api:1.0.0'
}
```

---

## Documentation

---

### Créer le service lors du chargement du plugin:

```java
import fr.aimcvent.inventory.ImplInventories;
import fr.aimcvent.inventory.api.Inventories;

public class YourPlugin extends JavaPlugin {
    private Inventories<YourPlugin> inventories;
    
    public void onEnable() {
        this.inventories = ImplInventories.create(this);
    }
}
```

---

### Créer un inventaire

Pour créer un inventaire, votre constructeur devra obligatoirement contenir ces trois paramètres dans l'ordre: `YourPlugin`, `Inventories<YourPlugin>`, `Player`

```java
import fr.aimcvent.inventory.AbstractInventory;
import fr.aimcvent.inventory.api.Inventories;

import org.bukkit.entity.Player;

public class YourInventory extends AbstractInventory<YourPlugin> {
    public YourInventory(YourPlugin plugin, Inventories<YourPlugin> inventories, Player player) {
        super(plugin, inventories, player, "Inventory Title", 6);
        // 6 = Nombre de ligne. Sachant qu'une ligne = 9 slots.
    }
    
    public void reload() {
        super.reload();
        
        for(int slot = 0; slot < 54; slot++) {
            this.add(slot, new CustomItem<>(this.player));
        }
    }
}
```

---

### Créer un item interactive

```java
import fr.aimcvent.inventory.api.Item;
import fr.aimcvent.inventory.api.Inventory;

import org.bukkit.entity.Player;

public class CustomItem implements Item<YourPlugin> {
    private final Player player;
    
    public CustomItem(Player player) {
        this.player = player;
    }
    
    @Override
    public ItemStack icon() {
        return new ItemStack(Material.STONE);
    }
    
    @Override
    public void click(Inventory<YourPlugin> inventory, ClickType clickType) {
        this.player.sendMessage(ChatColor.GREEN + "Vous venez de cliquer sur l'item!");
    }
}
```

---

### Créer un inventaire avec une pagination

```java
import fr.aimcvent.inventory.AbstractPaginationInventory;
import fr.aimcvent.inventory.api.Inventories;

import org.bukkit.entity.Player;

public class YourPaginateInventory extends AbstractPaginationInventory<YourPlugin> {
    public YourInventory(YourPlugin plugin, Inventories<YourPlugin> inventories, Player player) {
        super(plugin, inventories, player);
    }
    
    public void reload() {
        super.reload();
        
        // Ajouter 5 pages:
        for (int page = 1; page < 6; page++) {
            Inventory<YourPlugin> inventory = this.addPage(
                    new DefaultPageInventory(this.plugin, this.inventories, this.player, "Your Title [" + page + "/5]", 6 /* Nombre de ligne */)
            );

            for(int slot = 0; slot < 54; slot++) {
                inventory.add(slot, new CustomItem<>(this.player));
            }
        }
    }
    
    //Optionnel: Vous pouvez changer les items de pagination avec la ré-écriture de la methode
    @Override
    protected PaginateItem<T> paginateItem(boolean next) {
        return new CustomPaginateItem<>(this, next);
    }
}
```

### Créer un item de pagination personnalisé

```java
import fr.aimcvent.inventory.api.Inventory;
import fr.aimcvent.inventory.api.PaginateItem;
import fr.aimcvent.inventory.api.PaginationInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class CustomPaginateItem<T extends Plugin> implements PaginateItem<T> {
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
```

---

### Ouvrir l'inventaire à un joueur

```java
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {
    private final Inventories<YourPlugin> inventories;
    
    public PlayerInteractListener(Inventories<YourPlugin> inventories) {
        this.inventories = inventories;
    }
    
    @EventHandler
    private void on(PlayerInteractEvent event) {
        final ItemStack itemStack = event.getItem();
        if (itemStack != null && itemStack.getType() == Material.BANNER) {
            this.inventories.open(event.getPlayer(), YourInventory.class);
        }
    }
}
```