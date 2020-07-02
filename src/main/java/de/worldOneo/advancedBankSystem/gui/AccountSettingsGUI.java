package de.worldOneo.advancedBankSystem.gui;

import de.worldOneo.advancedBankSystem.bankItems.Account;
import de.worldOneo.advancedBankSystem.bankItems.BankAccount;
import de.worldOneo.advancedBankSystem.manager.BankAccountManager;
import de.worldOneo.advancedBankSystem.manager.GUIManager;
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
import java.util.function.Consumer;

public class AccountSettingsGUI extends AbstractGUI {
    private final HashMap<UUID, Account> uuidAccountHashMap = new HashMap<>();


    @Override
    public IGUI getInstance() {
        return this;
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
            final Player player = (Player) e.getWhoClicked();
            GUIManager.getInstance().getGui(YesNoGUI.class).open(player, o -> {
                if ((boolean) o) {
                    try {
                        player.closeInventory();
                        uuidAccountHashMap.get(e.getWhoClicked().getUniqueId()).delete().get();
                        player.sendMessage("Reloading Bank account!");
                        BankAccountManager.getInstance().removeAccount(player.getUniqueId());
                        BankAccount bankAccount = BankAccountManager.getInstance().createOrLoadAccount(player.getUniqueId().toString());
                        BankAccountManager.getInstance().addAccount(bankAccount);
                        player.sendMessage("Reloaded Bank account!");
                    } catch (InterruptedException | ExecutionException interruptedException) {
                        interruptedException.printStackTrace();
                        player.sendMessage("An error occurred!");
                    }

                } else {
                    e.getWhoClicked().closeInventory();
                }
            });
        }
        return super.handle(e);
    }

    @Override
    public String getGUITitle() {
        return Utils.colorize("&a&rSettings");
    }

    @Override
    public void open(Player player, Object accountObj, Consumer<Object> callback) {
        Account account = (Account) accountObj;
        uuidAccountHashMap.put(player.getUniqueId(), account);
        Inventory inventory = Bukkit.createInventory(null, 9, getGUITitle());
        inventory.setItem(4, Options.DELETE.getItemStack());
        player.openInventory(inventory);
    }
}
