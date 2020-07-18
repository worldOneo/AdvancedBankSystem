package de.worldOneo.advancedBankSystem.gui;

import de.worldOneo.advancedBankSystem.bankItems.Account;
import de.worldOneo.advancedBankSystem.bankItems.BankAccount;
import de.worldOneo.advancedBankSystem.manager.BankAccountManager;
import de.worldOneo.advancedBankSystem.utils.AccountSelectorInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class AccountSelectorGUI extends AbstractInputGUI<Account> {
    private BankAccount bankAccount;
    private AccountSelectorInfo accountSelectorInfo;
    private Account account;

    public AccountSelectorGUI(Player player) {
        super(player);
    }

    @Override
    public boolean handle(InventoryClickEvent e) {
        if (!GUIUtils.isHandleable(e)) {
            return false;
        }
        Account account = bankAccount.getAccount(e.getCurrentItem().getItemMeta().getDisplayName());
        if (account != null) {
            e.getWhoClicked().closeInventory();
            this.account = account;
            commitValue();
        }
        return super.handle(e);
    }


    @Override
    public Inventory render() {
        BankAccount bankAccount = BankAccountManager.getInstance().getBankAccount(accountSelectorInfo.player.getUniqueId());
        return GUIUtils.listAccounts(bankAccount.getAccounts(), getGUITitle(), accountSelectorInfo.showValue, accountSelectorInfo.showName);
    }

    @Override
    public String getGUITitle() {
        return "Select the account";
    }

    @Override
    public Account getValue() {
        return account;
    }

    public IGUI setAccountSelectorInfo(AccountSelectorInfo accountSelectorInfo) {
        this.accountSelectorInfo = accountSelectorInfo;
        return this;
    }

    public IGUI setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
        return this;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }
}
