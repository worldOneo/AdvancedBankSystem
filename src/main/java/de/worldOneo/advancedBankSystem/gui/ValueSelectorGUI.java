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
import java.util.List;
import java.util.stream.Collectors;

public class ValueSelectorGUI extends AbstractInputGUI<Integer> {
    private Integer value = 0;

    public ValueSelectorGUI(Player player) {
        super(player);
    }

    @Override
    public Integer getValue() {
        return value;
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


        if (menuItemsList.size() == 1) {
            int multiplier = (e.getClick() == ClickType.LEFT) ? 1 : -1;
            value += (multiplier * menuItemsList.get(0).getValue());
        } else if (e.getCurrentItem().getItemMeta().isUnbreakable() && e.getCurrentItem().getType() == Material.GOLD_INGOT) {
            commitValue();
            return super.handle(e);
        }
        open();
        return super.handle(e);
    }

    @Override
    public Inventory render() {
        return createInventory(getPlayer());
    }

    private Inventory createInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, getGUITitle());
        Arrays.stream(MenuItems.values()).forEach(menuItems -> inventory.addItem(menuItems.getItemStack()));
        ItemStack itemStack = new ItemStack(Material.GOLD_INGOT);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(value.toString());
        itemMeta.setUnbreakable(true);
        itemStack.setItemMeta(itemMeta);
        inventory.addItem(itemStack);
        return inventory;
    }

    @Override
    public String getGUITitle() {
        return "Select Value";
    }
}
