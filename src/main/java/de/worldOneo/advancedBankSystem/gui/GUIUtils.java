package de.worldOneo.advancedBankSystem.gui;

import de.worldOneo.advancedBankSystem.bankItems.Account;
import de.worldOneo.advancedBankSystem.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collection;

public class GUIUtils {
    public static Inventory listAccounts(Collection<Account> accounts, String title, boolean showValue, boolean showName) {
        float accountCount = accounts.size();
        int size = (int) Math.ceil(accountCount / 9) * 9;
        size = size == 0 ? 9 : size;
        Inventory inventory = Bukkit.createInventory(null, size, title);
        accounts.forEach(account -> {
            ItemStack itemStack = new ItemStack(Material.GOLD_NUGGET);
            ItemMeta itemMeta = itemStack.getItemMeta();
            assert itemMeta != null;
            itemMeta.setDisplayName(account.getId());
            itemMeta.setLore(Arrays.asList(Utils.colorize(showValue ? "&6" + account.getValue() + "$" : ""), showName ? account.getName() : ""));
            itemStack.setItemMeta(itemMeta);
            inventory.addItem(itemStack);
        });
        return inventory;
    }

    public static Inventory listAccounts(Collection<Account> accounts, String title) {
        return listAccounts(accounts, title, true);
    }

    public static Inventory listAccounts(Collection<Account> accounts, String title, boolean showValue) {
        return listAccounts(accounts, title, showValue, true);
    }


    public static boolean isHandleable(InventoryClickEvent e) {
        return !(e.getCurrentItem() == null || e.getClickedInventory() == null || e.getCurrentItem().getItemMeta() == null);
    }
}
