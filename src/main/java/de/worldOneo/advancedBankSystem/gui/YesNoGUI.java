package de.worldOneo.advancedBankSystem.gui;

import de.worldOneo.advancedBankSystem.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class YesNoGUI extends AbstractInputGUI<Boolean> {
    private String description = "Confirm!";
    private boolean confirmed = false;

    public YesNoGUI(Player player) {
        super(player);
    }

    @Override
    public String getGUITitle() {
        return "Confirm?";
    }

    @Override
    public Boolean getValue() {
        return confirmed;
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
    public boolean handle(InventoryClickEvent e) {
        e.setCancelled(true);
        if (e.getCurrentItem() == null) {
            return false;
        }
        if (e.getCurrentItem().equals(YesNo.YES.getItemStack())) {
            confirmed = true;
            commitValue();
        } else if (e.getCurrentItem().equals(YesNo.NO.getItemStack())) {
            confirmed = false;
            commitValue();
        }
        return super.handle(e);
    }

    @Override
    public Inventory render() {
        Inventory inventory = Bukkit.createInventory(null, 9, getGUITitle());
        inventory.setItem(3, YesNo.YES.getItemStack());
        inventory.setItem(4, Utils.getNamedItem(Material.PAPER, description));
        inventory.setItem(5, YesNo.NO.getItemStack());
        return inventory;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
