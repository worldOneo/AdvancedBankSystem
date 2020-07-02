package de.worldOneo.advancedBankSystem.manager;

import de.worldOneo.advancedBankSystem.bankItems.BankAccount;
import de.worldOneo.advancedBankSystem.utils.TableCreationStrings;
import de.worldOneo.advancedBankSystem.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BankAccountManager {
    private final Map<String, BankAccount> idBankAccountMap = new HashMap<>();

    private static final BankAccountManager instance = new BankAccountManager();

    private BankAccountManager() {
    }

    public BankAccount getBankAccount(String id) {
        return idBankAccountMap.get(id);
    }

    public BankAccount getBankAccount(UUID id) {
        return getBankAccount(id.toString());
    }

    public boolean addAccount(String id) {
        BankAccount bankAccount = new BankAccount(id);
        if (idBankAccountMap.containsKey(id)) {
            return false;
        }
        bankAccount.load(id);
        idBankAccountMap.put(id, bankAccount);
        return true;
    }

    public boolean addAccount(BankAccount bankAccount) {
        if (idBankAccountMap.containsKey(bankAccount.getOwnerId())) {
            return false;
        }
        idBankAccountMap.put(bankAccount.getOwnerId(), bankAccount);
        return true;
    }

    public BankAccount createOrLoadAccount(String ownerId) {
        MySQLManager.getInstance().executeUpdateSync(
                String.format("INSERT IGNORE INTO `%s` (id, UUID) VALUES ('%s', '%s');",
                        TableCreationStrings.CUSTOMER.getTableName(), Utils.generateId(), ownerId
                )
        );
        BankAccount bankAccount = new BankAccount(ownerId);
        bankAccount.load(ownerId);
        return bankAccount;
    }

    public BankAccount removeAccount(String id) {
        return idBankAccountMap.remove(id);
    }

    public Map<String, BankAccount> getIdBankAccountMap() {
        return idBankAccountMap;
    }

    public static BankAccountManager getInstance() {
        return instance;
    }
}
