package de.worldOneo.advancedBankSystem.gui;

import de.worldOneo.advancedBankSystem.manager.GUIManager;
import de.worldOneo.advancedBankSystem.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class BankGUI extends AbstractGUI {

    private enum MenuItems {
        MY_ACCOUNT(Utils.getNamedItem(Material.GOLD_BLOCK, "My Account")),
        PAY_OTHER(Utils.getNamedItem(Material.RED_DYE, "Pay Someone"));

        private final ItemStack itemStack;

        MenuItems(ItemStack itemStack) {
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
    public String getGUITitle() {
        return Utils.colorize("&a&rBankSystem");
    }

    @Override
    public boolean handle(InventoryClickEvent e) {
        if (!GUIUtils.isHandleable(e)) {
            return false;
        }
        ItemStack itemStack = e.getCurrentItem();
        if (itemStack.equals(MenuItems.MY_ACCOUNT.getItemStack())) {
            GUIManager.getInstance().getGui(InfoGUI.class).open((Player) e.getWhoClicked(), o -> {
            });
        } else if (itemStack.equals(MenuItems.PAY_OTHER.getItemStack())) {
            GUIManager.getInstance().getGui(PlayerSelectorGUI.class).open((Player) e.getWhoClicked(), o -> {
            });
        }
        return super.handle(e);
    }

    /**
     * @param callback doesn't use the callback
     */
    @Override
    public void open(Player player, Consumer<Object> callback) {
        Inventory inventory = Bukkit.createInventory(null, 9, getGUITitle());
        inventory.setItem(3, MenuItems.MY_ACCOUNT.getItemStack());
        inventory.setItem(5, MenuItems.PAY_OTHER.getItemStack());
        player.openInventory(inventory);
    }
}
