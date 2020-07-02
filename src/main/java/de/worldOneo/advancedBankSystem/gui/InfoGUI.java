package de.worldOneo.advancedBankSystem.gui;

import de.worldOneo.advancedBankSystem.bankItems.Account;
import de.worldOneo.advancedBankSystem.manager.BankAccountManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Collection;
import java.util.function.Consumer;

public class InfoGUI extends AbstractGUI {
    @Override
    public IGUI getInstance() {
        return this;
    }

    @Override
    public boolean handle(InventoryClickEvent e) {
        e.setCancelled(true);
        return true;
    }

    @Override
    public void open(Player player, Consumer<Object> callback) {
        Collection<Account> accounts = BankAccountManager.getInstance().getBankAccount(player.getUniqueId()).getAccounts();
        player.openInventory(GUIUtils.listAccounts(accounts, getGUITitle()));
    }

    @Override
    public String getGUITitle() {
        return "Your bank accounts";
    }
}
