package de.worldOneo.advancedBankSystem.gui;

import de.worldOneo.advancedBankSystem.bankItems.Account;
import de.worldOneo.advancedBankSystem.bankItems.BankAccount;
import de.worldOneo.advancedBankSystem.manager.BankAccountManager;
import de.worldOneo.advancedBankSystem.manager.GUIManager;
import de.worldOneo.advancedBankSystem.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public class PayGUI extends AbstractGUI {
    private final HashMap<UUID, Consumer<Object>> consumerHashMap = new HashMap<>();
    private final HashMap<UUID, BankAccount> bankAccountMap = new HashMap<>();

    @Override
    public PayGUI getInstance() {
        return this;
    }

    @Override
    public boolean handle(InventoryClickEvent e) {
        e.setCancelled(true);
        if (e.getCurrentItem() == null || e.getClickedInventory() == null || e.getCurrentItem().getItemMeta() == null) {
            return false;
        }
        Player player = (Player) e.getWhoClicked();
        Account account = bankAccountMap.get(player.getUniqueId())
                .getAccount(e.getCurrentItem().getItemMeta().getDisplayName());
        if (account != null) {
            GUIManager.getInstance().getGui(YesNoGUI.class).getInstance().open((Player) e.getWhoClicked(), "Confirm the Transaction", o -> {
                boolean yesno = (Boolean) o;
                if (yesno) {
                    consumerHashMap.get(player.getUniqueId()).accept(account);
                }
                player.closeInventory();
            });
        }
        return true;
    }

    /**
     * @param callback fires the callback with the selected account.
     */
    @Override
    public void open(Player player, Object objFrom, Consumer<Object> callback) {
        Player from = (Player) objFrom;
        consumerHashMap.put(player.getUniqueId(), callback);
        BankAccount bankAccount = BankAccountManager.getInstance().getBankAccount(from.getUniqueId());
        bankAccountMap.put(player.getUniqueId(), bankAccount);
        Collection<Account> accounts = bankAccount.getAccounts();
        player.openInventory(GUIUtils.listAccounts(accounts, getGUITitle()));
    }

    @Override
    public String getGUITitle() {
        return Utils.colorize("&a&rSelect Account to Pay");
    }
}
