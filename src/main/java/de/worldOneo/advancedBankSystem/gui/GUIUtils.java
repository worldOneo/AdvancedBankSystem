package de.worldOneo.advancedBankSystem.gui;

import de.worldOneo.advancedBankSystem.bankItems.Account;
import de.worldOneo.advancedBankSystem.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collection;

public class GUIUtils {
    public static Inventory listAccounts(Collection<Account> accounts, String title) {
        float accountCount = accounts.size();
        Inventory inventory = Bukkit.createInventory(null, (int) Math.ceil(accountCount / 9) * 9, title);
        accounts.forEach(account -> {
            ItemStack itemStack = new ItemStack(Material.GOLD_NUGGET);
            ItemMeta itemMeta = itemStack.getItemMeta();
            assert itemMeta != null;
            itemMeta.setDisplayName(account.getId());
            itemMeta.setLore(Arrays.asList(Utils.colorize("&6" + account.getValue() + "$"), account.getName()));
            itemStack.setItemMeta(itemMeta);
            inventory.addItem(itemStack);
        });
        return inventory;
    }
}
