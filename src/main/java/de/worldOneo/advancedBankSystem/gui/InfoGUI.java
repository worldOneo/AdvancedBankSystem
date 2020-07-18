package de.worldOneo.advancedBankSystem.gui;

import de.worldOneo.advancedBankSystem.bankItems.Account;
import de.worldOneo.advancedBankSystem.manager.BankAccountManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Collection;

public class InfoGUI extends AbstractGUI {

    public InfoGUI(Player player) {
        super(player);
    }

    @Override
    public boolean handle(InventoryClickEvent e) {
        if (!GUIUtils.isHandleable(e)) {
            return false;
        }
        Player player = (Player) e.getWhoClicked();
        Account account = BankAccountManager.getInstance().getBankAccount(player.getUniqueId()).getAccount(e.getCurrentItem().getItemMeta().getDisplayName());
        if (account != null) {
            AccountSettingsGUI accountSettingsGUI = new AccountSettingsGUI(getPlayer());
            accountSettingsGUI.setAccount(account);
            accountSettingsGUI.open();
        }
        return super.handle(e);
    }

    @Override
    public Inventory render() {
        Collection<Account> accounts = BankAccountManager.getInstance().getBankAccount(getPlayer().getUniqueId()).getAccounts();
        return GUIUtils.listAccounts(accounts, getGUITitle());
    }

    @Override
    public String getGUITitle() {
        return "Your bank accounts";
    }
}
