package de.worldOneo.advancedBankSystem.gui;

import de.worldOneo.advancedBankSystem.bankItems.Account;
import de.worldOneo.advancedBankSystem.bankItems.BankAccount;
import de.worldOneo.advancedBankSystem.manager.BankAccountManager;
import de.worldOneo.advancedBankSystem.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class AccountSettingsGUI extends AbstractGUI {
    private final HashMap<UUID, Account> uuidAccountHashMap = new HashMap<>();
    private Account account;

    public AccountSettingsGUI(Player player) {
        super(player);
    }

    private enum Options {
        DELETE(Utils.getNamedItem(Material.CAULDRON, "Delete this account"));
        private final ItemStack itemStack;

        Options(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }
    }

    @Override
    public boolean handle(InventoryClickEvent e) {
        if (e.getCurrentItem() == null || e.getClickedInventory() == null || e.getCurrentItem().getItemMeta() == null) {
            return false;
        }
        if (e.getCurrentItem().equals(Options.DELETE.getItemStack())) {
            YesNoGUI yesNoGUI = new YesNoGUI(getPlayer());
            yesNoGUI.setSuccessConsumer(this::onMessageConfirm);
            yesNoGUI.open();
        }
        return super.handle(e);
    }

    @Override
    public String getGUITitle() {
        return "Settings";
    }

    @Override
    public Inventory render() {
        Inventory inventory = Bukkit.createInventory(null, 9, getGUITitle());
        inventory.setItem(4, Options.DELETE.getItemStack());
        return inventory;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    private void onMessageConfirm(Boolean confirmed) {
        Player player = getPlayer();
        player.closeInventory();
        if (confirmed) {
            try {
                account.delete().get();
                player.sendMessage("Reloading Bank account!");
                BankAccountManager.getInstance().removeAccount(player.getUniqueId());
                BankAccount bankAccount = BankAccountManager.getInstance().createOrLoadAccount(player.getUniqueId().toString());
                BankAccountManager.getInstance().addAccount(bankAccount);
                player.sendMessage("Reloaded Bank account!");
            } catch (InterruptedException | ExecutionException interruptedException) {
                interruptedException.printStackTrace();
                player.sendMessage("An error occurred!");
            }

        }
    }
}
