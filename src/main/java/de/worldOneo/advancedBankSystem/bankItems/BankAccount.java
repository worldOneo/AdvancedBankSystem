package de.worldOneo.advancedBankSystem.bankItems;

import de.worldOneo.advancedBankSystem.manager.MySQLManager;
import de.worldOneo.advancedBankSystem.utils.TableCreationStrings;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BankAccount implements IBankItem, IStoreable {
    private final Map<String, Account> accountHashMap = new HashMap<>();
    private String id;
    private final String ownerId;

    public BankAccount(String ownerId) {
        this.ownerId = ownerId;
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getOwnerId() {
        return ownerId;
    }

    public Map<String, Account> getAccountHashMap() {
        return accountHashMap;
    }

    public Collection<Account> getAccounts() {
        return accountHashMap.values();
    }

    public Account getAccount(String id) {
        return accountHashMap.get(id);
    }

    public void addAccounts(Account... accounts) {
        for (Account account : accounts) {
            accountHashMap.put(account.getId(), account);
        }
    }

    @Override
    public void save() {
        accountHashMap.values().forEach(IStoreable::save);
        MySQLManager.getInstance().executeUpdate(
                String.format("INSERT IGNORE INTO `%s` (id, UUID) VALUES ('%s','%s');",
                        TableCreationStrings.CUSTOMER.getTableName(), this.id, this.ownerId)
        );
    }

    @Override
    public boolean load(String id) {
        ResultSet resultSet = MySQLManager.getInstance().executeQuery(String.format("SELECT * FROM `%s` WHERE UUID='%s';",
                TableCreationStrings.CUSTOMER.getTableName(), ownerId));
        try {
            resultSet.next();
            this.id = resultSet.getString("id");
            resultSet = MySQLManager.getInstance().executeQuery(String.format("SELECT * FROM `%s` WHERE IDOwner='%s';",
                    TableCreationStrings.ACCOUNTS.getTableName(), this.id));
            while (resultSet.next()) {
                String accountID = resultSet.getString("id");
                Account account = new Account(this.id);
                account.load(accountID);
                accountHashMap.put(accountID, account);
            }
            return true;
        } catch (SQLException throwables) {
            return false;
        }
    }
}
