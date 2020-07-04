package de.worldOneo.advancedBankSystem.gui;

import de.worldOneo.advancedBankSystem.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ValueSelectorGUI extends AbstractGUI {
    private final HashMap<UUID, Integer> uuidValueHashMap = new HashMap<>();
    private final HashMap<UUID, Consumer<Object>> uuidConsumerHashMap = new HashMap<>();

    @Override
    public IGUI getInstance() {
        return this;
    }

    private enum MenuItems {
        p1(Utils.getNamedItem(Material.GREEN_DYE, "1"), 1),
        p10(Utils.getNamedItem(Material.GREEN_DYE, "10"), 10),
        p20(Utils.getNamedItem(Material.GREEN_DYE, "20"), 20),
        p50(Utils.getNamedItem(Material.GREEN_DYE, "50"), 50),
        p100(Utils.getNamedItem(Material.GREEN_DYE, "100"), 100),
        p500(Utils.getNamedItem(Material.GREEN_DYE, "500"), 500),
        p1000(Utils.getNamedItem(Material.GREEN_DYE, "1000"), 1000),
        p10000(Utils.getNamedItem(Material.GREEN_DYE, "10000"), 10000),
        ;

        private final ItemStack itemStack;
        private final int value;

        MenuItems(ItemStack itemStack, int value) {
            this.itemStack = itemStack;
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }
    }

    @Override
    public boolean handle(InventoryClickEvent e) {
        if (!GUIUtils.isHandleable(e)) {
            return false;
        }
        List<MenuItems> menuItemsList = Arrays.stream(MenuItems.values())
                .filter(menuItems -> menuItems.getItemStack().equals(e.getCurrentItem()))
                .collect(Collectors.toList());
        UUID uuid = e.getWhoClicked().getUniqueId();
        if (menuItemsList.size() == 1) {
            int multiplier;
            if (e.getClick() == ClickType.LEFT) {
                multiplier = 1;
            } else {
                multiplier = -1;
            }
            uuidValueHashMap.put(uuid, uuidValueHashMap.get(uuid) + (multiplier * menuItemsList.get(0).getValue()));
        } else if (e.getCurrentItem().getItemMeta().isUnbreakable() && e.getCurrentItem().getType() == Material.GOLD_INGOT) {
            uuidConsumerHashMap.get(uuid).accept(uuidValueHashMap.get(uuid));
            return super.handle(e);
        }
        e.getWhoClicked().openInventory(createInventory((Player) e.getWhoClicked()));
        return super.handle(e);
    }

    /**
     * @param callback is called with the selected value.
     */
    @Override
    public void open(Player player, Consumer<Object> callback) {
        uuidConsumerHashMap.put(player.getUniqueId(), callback);
        uuidValueHashMap.put(player.getUniqueId(), 0);
        player.openInventory(createInventory(player));
    }

    private Inventory createInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, getGUITitle());
        Arrays.stream(MenuItems.values()).forEach(menuItems -> inventory.addItem(menuItems.getItemStack()));
        ItemStack itemStack = new ItemStack(Material.GOLD_INGOT);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(uuidValueHashMap.get(player.getUniqueId()).toString());
        itemMeta.setUnbreakable(true);
        itemStack.setItemMeta(itemMeta);
        inventory.addItem(itemStack);
        return inventory;
    }

    @Override
    public String getGUITitle() {
        return Utils.colorize("&a&rSelect Value");
    }
}
