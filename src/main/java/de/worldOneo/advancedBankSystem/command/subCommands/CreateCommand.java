package de.worldOneo.advancedBankSystem.command.subCommands;

import de.worldOneo.advancedBankSystem.bankItems.Account;
import de.worldOneo.advancedBankSystem.bankItems.BankAccount;
import de.worldOneo.advancedBankSystem.manager.BankAccountManager;
import org.bukkit.entity.Player;

public class CreateCommand implements PlayerSubCommand {
    private static final CreateCommand instance = new CreateCommand();

    private CreateCommand() {

    }

    public static CreateCommand getInstance() {
        return instance;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        String ownerId = player.getUniqueId().toString();
        BankAccount bankAccount = BankAccountManager.getInstance().getBankAccount(ownerId);
        Account account;
        if (args.length == 2) {
            account = Account.newAccount(bankAccount.getId(), args[1]);
        } else {
            System.out.println("Create unnamed account!");
            account = Account.newAccount(bankAccount.getId());
        }
        bankAccount.addAccounts(account);
        player.sendMessage(String.format("Account %s (%s) was for the BankAccount %s created!", account.getName(), account.getId(), bankAccount.getId()));
        return true;
    }
}
