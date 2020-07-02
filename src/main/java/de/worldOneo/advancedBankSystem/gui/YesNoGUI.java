package de.worldOneo.advancedBankSystem.gui;

import de.worldOneo.advancedBankSystem.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public class YesNoGUI extends AbstractGUI {
    private HashMap<UUID, Consumer<Object>> consumerHashMap = new HashMap<>();

    @Override
    public String getGUITitle() {
        return Utils.colorize("&a&rConfirm?");
    }

    public enum YesNo {
        YES(Utils.getNamedItem(Material.GREEN_DYE, "Yes!")),
        NO(Utils.getNamedItem(Material.RED_DYE, "No!"));

        private final ItemStack itemStack;

        YesNo(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }
    }

    @Override
    public IGUI getInstance() {
        return this;
    }

    @Override
    public boolean handle(InventoryClickEvent e) {
        e.setCancelled(true);
        if (e.getCurrentItem() == null) {
            return false;
        }
        if (e.getCurrentItem().equals(YesNo.YES.getItemStack())) {
            consumerHashMap.get(e.getWhoClicked().getUniqueId()).accept(true);
        } else if (e.getCurrentItem().equals(YesNo.NO.getItemStack())) {
            consumerHashMap.get(e.getWhoClicked().getUniqueId()).accept(false);
        }
        return super.handle(e);
    }

    @Override
    public void open(Player player, Object descriptionObj, Consumer<Object> callback) {
        String description = (String) descriptionObj;
        consumerHashMap.put(player.getUniqueId(), callback);
        Inventory inventory = Bukkit.createInventory(null, 9, getGUITitle());
        inventory.setItem(3, YesNo.YES.getItemStack());
        inventory.setItem(4, Utils.getNamedItem(Material.PAPER, description));
        inventory.setItem(5, YesNo.NO.getItemStack());
        player.openInventory(inventory);
    }

    @Override
    public void open(Player player, Consumer<Object> callback) {
        open(player, "Confirm!", callback);
    }
}
