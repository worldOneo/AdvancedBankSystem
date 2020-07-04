package de.worldOneo.advancedBankSystem.gui;

import de.worldOneo.advancedBankSystem.bankItems.Account;
import de.worldOneo.advancedBankSystem.bankItems.BankAccount;
import de.worldOneo.advancedBankSystem.manager.BankAccountManager;
import de.worldOneo.advancedBankSystem.utils.AccountSelectorInfo;
import de.worldOneo.advancedBankSystem.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public class AccountSelectorGUI extends AbstractGUI {
    private final HashMap<UUID, Consumer<Object>> consumerHashMap = new HashMap<>();
    private final HashMap<UUID, BankAccount> bankAccountMap = new HashMap<>();

    @Override
    public IGUI getInstance() {
        return this;
    }

    @Override
    public boolean handle(InventoryClickEvent e) {
        if (!GUIUtils.isHandleable(e)) {
            return false;
        }
        Account account = bankAccountMap.get(e.getWhoClicked().getUniqueId()).getAccount(e.getCurrentItem().getItemMeta().getDisplayName());
        if (account != null) {
            e.getWhoClicked().closeInventory();
            consumerHashMap.get(e.getWhoClicked().getUniqueId()).accept(account);
        }
        return super.handle(e);
    }

    /**
     * @param selectorInfoObj a AccountSelectorInfo object.
     * @param callback        is called with the selected account
     */
    @Override
    public void open(Player player, Object selectorInfoObj, Consumer<Object> callback) {
        consumerHashMap.put(player.getUniqueId(), callback);
        AccountSelectorInfo accountSelectorInfo = (AccountSelectorInfo) selectorInfoObj;
        BankAccount bankAccount = BankAccountManager.getInstance().getBankAccount(accountSelectorInfo.player.getUniqueId());
        bankAccountMap.put(player.getUniqueId(), bankAccount);
        Inventory inventory = GUIUtils.listAccounts(bankAccount.getAccounts(), getGUITitle(), accountSelectorInfo.showValue, accountSelectorInfo.showName);
        player.openInventory(inventory);
    }

    @Override
    public String getGUITitle() {
        return Utils.colorize("&a&rSelect the account");
    }
}
