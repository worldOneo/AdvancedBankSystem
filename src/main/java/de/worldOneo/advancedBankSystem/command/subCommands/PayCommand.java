package de.worldOneo.advancedBankSystem.command.subCommands;

import de.worldOneo.advancedBankSystem.BankSystem;
import de.worldOneo.advancedBankSystem.bankItems.Account;
import de.worldOneo.advancedBankSystem.bankItems.Transaction;
import de.worldOneo.advancedBankSystem.gui.PayGUI;
import de.worldOneo.advancedBankSystem.manager.BankAccountManager;
import de.worldOneo.advancedBankSystem.manager.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class PayCommand implements PlayerSubCommand {
    private final HashMap<UUID, Account> uuidAccountIDTempMap = new HashMap<>();

    private static final PayCommand instance = new PayCommand();

    private PayCommand() {

    }

    public static PayCommand getInstance() {
        return instance;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 4) {
            player.sendMessage(Objects.requireNonNull(BankSystem.getInstance().getConfig().getString("help.pay")));
            return true;
        }
        Player playerTo = Bukkit.getPlayer(args[1]);
        if (playerTo == null) {
            player.sendMessage("The player is not online!");
            return true;
        }
        final long value;
        try {
            value = Long.parseLong(args[3]);
        } catch (NumberFormatException e) {
            player.sendMessage("Amount is not an valid number");
            return true;
        }
        StringBuilder reason = new StringBuilder();
        for (int i = 4; i < args.length; i++) {
            reason.append(" ").append(args[i]);
        }
        uuidAccountIDTempMap.put(player.getUniqueId(), BankAccountManager.getInstance().getBankAccount(playerTo.getUniqueId().toString()).getAccount(args[2]));
        GUIManager.getInstance().getGui(PayGUI.class).open(player, playerTo, o -> {
            Account accountFrom = (Account) o;
            Account accountTo = uuidAccountIDTempMap.get(player.getUniqueId());
            Transaction transaction = accountFrom.makeTransaction(accountTo, value, reason.toString());
            if (transaction != null) {
                accountTo.addValue(value);
                player.sendMessage(String.format("Transaction successful! (%s) -[%d$]-> (%s)",
                        transaction.getIdFrom(), transaction.getValue(), transaction.getIdTo()));
            } else {
                player.sendMessage("Transaction failed!");
            }
            System.out.println(accountFrom.getName() + " (" + accountFrom.getId() + ") -[" + value + "$]-> " + accountTo.getName() + " (" + accountTo.getId() + ") : " + reason);
        });
        return true;
    }
}
