package de.worldOneo.advancedBankSystem.gui;

import de.worldOneo.advancedBankSystem.bankItems.Account;
import de.worldOneo.advancedBankSystem.manager.BankAccountManager;
import de.worldOneo.advancedBankSystem.manager.GUIManager;
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
        if (e.getCurrentItem() == null || e.getClickedInventory() == null || e.getCurrentItem().getItemMeta() == null) {
            return false;
        }
        Player player = (Player) e.getWhoClicked();
        Account account = BankAccountManager.getInstance().getBankAccount(player.getUniqueId()).getAccount(e.getCurrentItem().getItemMeta().getDisplayName());
        if(account != null){
            GUIManager.getInstance().getGui(AccountSettingsGUI.class).open(player, account, o -> {});
        }
        return super.handle(e);
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
