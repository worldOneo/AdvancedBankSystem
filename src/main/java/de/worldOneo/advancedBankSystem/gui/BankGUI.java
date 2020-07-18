package de.worldOneo.advancedBankSystem.gui;

import de.worldOneo.advancedBankSystem.bankItems.Account;
import de.worldOneo.advancedBankSystem.bankItems.Transaction;
import de.worldOneo.advancedBankSystem.manager.BankAccountManager;
import de.worldOneo.advancedBankSystem.utils.AccountSelectorInfo;
import de.worldOneo.advancedBankSystem.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BankGUI extends AbstractGUI {
    private Account accountTo;
    private Account accountFrom;
    private int value;
    private Player playerTo;

    public BankGUI(Player player) {
        super(player);
    }

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
    public String getGUITitle() {
        return "BankSystem";
    }

    @Override
    public boolean handle(InventoryClickEvent e) {
        if (!GUIUtils.isHandleable(e)) {
            return false;
        }
        ItemStack itemStack = e.getCurrentItem();
        if (itemStack.equals(MenuItems.MY_ACCOUNT.getItemStack())) {
            InfoGUI infoGUI = new InfoGUI(getPlayer());
            infoGUI.open();
            return super.handle(e);
        } else if (!itemStack.equals(MenuItems.PAY_OTHER.getItemStack())) {
            e.setCancelled(true);
            return false;
        }
        PlayerSelectorGUI playerSelectorGUI = new PlayerSelectorGUI(getPlayer());
        playerSelectorGUI.setSuccessConsumer(this::onPlayerSelected);
        playerSelectorGUI.open();
        return super.handle(e);
    }

    @Override
    public Inventory render() {
        Inventory inventory = Bukkit.createInventory(null, 9, getGUITitle());
        inventory.setItem(3, MenuItems.MY_ACCOUNT.getItemStack());
        inventory.setItem(5, MenuItems.PAY_OTHER.getItemStack());
        return inventory;
    }

    public void onPlayerSelected(Player player) {
        playerTo = player;
        AccountSelectorGUI accountSelectorGUI = new AccountSelectorGUI(getPlayer());
        accountSelectorGUI.setAccountSelectorInfo(new AccountSelectorInfo(player, false, true));
        accountSelectorGUI.setBankAccount(BankAccountManager.getInstance().getBankAccount(player.getUniqueId()));
        accountSelectorGUI.setSuccessConsumer(this::onAccountSelected);
        accountSelectorGUI.open();
    }

    private void onAccountSelected(Account account) {
        accountTo = account;
        ValueSelectorGUI valueSelectorGUI = new ValueSelectorGUI(getPlayer());
        valueSelectorGUI.setSuccessConsumer(this::onValueSelected);
        valueSelectorGUI.open();
    }

    private void onValueSelected(Integer integer) {
        if (integer <= 0) {
            return;
        }
        value = integer;
        AccountSelectorGUI accountSelectorGUI = new AccountSelectorGUI(getPlayer());
        accountSelectorGUI.setAccountSelectorInfo(new AccountSelectorInfo(getPlayer(), true, true));
        accountSelectorGUI.setBankAccount(BankAccountManager.getInstance().getBankAccount(getPlayer().getUniqueId()));
        accountSelectorGUI.setSuccessConsumer(this::onAccountFromSelected);
        accountSelectorGUI.open();
    }

    private void onAccountFromSelected(Account account) {
        accountFrom = account;
        YesNoGUI yesNoGUI = new YesNoGUI(getPlayer());
        String format = String.format("%s (%s) -[%d$]-> %s (%s)", getPlayer().getDisplayName(), accountFrom.getId(), value, playerTo.getDisplayName(), accountFrom.getId());
        yesNoGUI.setDescription(format);
        yesNoGUI.setSuccessConsumer(this::onTransactionConfirm);
        yesNoGUI.open();
    }

    private void onTransactionConfirm(Boolean confirmed) {
        String format = String.format("%s (%s) -[%d$]-> %s (%s)", getPlayer().getDisplayName(), accountFrom.getId(), value, playerTo.getDisplayName(), accountFrom.getId());
        if (confirmed) {
            Transaction transaction = accountFrom.makeTransaction(accountTo, value);
            if (transaction != null) {
                String message = "Transaction successful! (" + format + ")";
                getPlayer().sendMessage(message);
                playerTo.sendMessage(message);
            } else {
                getPlayer().sendMessage("Transaction failed");
            }
        }
        getPlayer().closeInventory();
    }
}
