package de.worldOneo.advancedBankSystem.gui;

import de.worldOneo.advancedBankSystem.bankItems.Account;
import de.worldOneo.advancedBankSystem.bankItems.Transaction;
import de.worldOneo.advancedBankSystem.manager.GUIManager;
import de.worldOneo.advancedBankSystem.utils.AccountSelectorInfo;
import de.worldOneo.advancedBankSystem.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class BankGUI extends AbstractGUI {

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
    public IGUI getInstance() {
        return this;
    }

    @Override
    public String getGUITitle() {
        return Utils.colorize("&a&rBankSystem");
    }

    @Override
    public boolean handle(InventoryClickEvent e) {
        if (!GUIUtils.isHandleable(e)) {
            return false;
        }
        ItemStack itemStack = e.getCurrentItem();
        Player clicker = (Player) e.getWhoClicked();
        GUIManager guiManager = GUIManager.getInstance();
        if (itemStack.equals(MenuItems.MY_ACCOUNT.getItemStack())) {
            guiManager.getGui(InfoGUI.class).open((Player) e.getWhoClicked(), o -> {
            });
        } else if (itemStack.equals(MenuItems.PAY_OTHER.getItemStack())) {
            guiManager.getGui(PlayerSelectorGUI.class).open(clicker, o -> {
                Player player = (Player) o;
                guiManager.getGui(AccountSelectorGUI.class).open(clicker, new AccountSelectorInfo(player, false, true),
                        o1 -> {
                            Account account = (Account) o1;
                            guiManager.getGui(ValueSelectorGUI.class).open(clicker, o2 -> {
                                int value = (int) o2;
                                if (value <= 0) {
                                    return;
                                }
                                guiManager.getGui(AccountSelectorGUI.class)
                                        .open(clicker, new AccountSelectorInfo(clicker, true, true), o3 -> {
                                            Account accountFrom = (Account) o3;
                                            String format = String.format("%s (%s) -[%d$]-> %s (%s)", clicker.getDisplayName(), accountFrom.getId(), value, player.getDisplayName(), account.getId());
                                            guiManager.getGui(YesNoGUI.class).open(clicker, format, o4 -> {
                                                boolean transfer = (Boolean) o4;
                                                if (transfer) {
                                                    Transaction transaction = accountFrom.makeTransaction(account, value);
                                                    if (transaction != null) {
                                                        String message = "Transaction successful! (" + format + ")";
                                                        clicker.sendMessage(message);
                                                        player.sendMessage(message);
                                                    } else {
                                                        clicker.sendMessage("Transaction failed");
                                                    }
                                                }
                                                clicker.closeInventory();
                                            });
                                        });
                            });
                        });
            });
        }
        return super.handle(e);
    }

    /**
     * @param callback doesn't use the callback
     */
    @Override
    public void open(Player player, Consumer<Object> callback) {
        Inventory inventory = Bukkit.createInventory(null, 9, getGUITitle());
        inventory.setItem(3, MenuItems.MY_ACCOUNT.getItemStack());
        inventory.setItem(5, MenuItems.PAY_OTHER.getItemStack());
        player.openInventory(inventory);
    }
}
