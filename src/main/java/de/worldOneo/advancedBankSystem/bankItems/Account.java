package de.worldOneo.advancedBankSystem.bankItems;


import de.worldOneo.advancedBankSystem.manager.MySQLManager;
import de.worldOneo.advancedBankSystem.utils.TableCreationStrings;
import de.worldOneo.advancedBankSystem.utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class Account implements IBankItem, IValue, IStoreable {
    private final List<Transaction> transactionList = new ArrayList<>();
    private String id;
    private String name;
    private final String ownerId;
    private long value;

    public Account(String ownerId) {
        this.ownerId = ownerId;
    }

    private Account(String ownerId, String id) {
        this(ownerId, id, "Unnamed");
    }

    private Account(String ownerId, String id, String name) {
        this.ownerId = ownerId;
        this.id = id;
        this.name = name;
    }

    public static Account newAccount(String ownerId, String name) {
        return new Account(ownerId, Utils.generateId(), name);
    }

    public static Account newAccount(String ownerId) {
        return newAccount(ownerId, "unnamed");
    }


    public Transaction makeTransaction(Account to, long value, String reason) {
        if (value > 0 && this.value >= value) {
            Transaction transaction = new Transaction(this.getId(), to.getId(), value, System.currentTimeMillis(), reason);
            transactionList.add(transaction);
            remValue(value);
            return transaction;
        }
        return null;
    }

    public Transaction makeTransaction(Account to, long value) {
        return makeTransaction(to, value, "Unknown");
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getOwnerId() {
        return ownerId;
    }

    @Override
    public long getValue() {
        return value;
    }

    @Override
    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public void addValue(long value) {
        this.value += value;
    }

    @Override
    public void remValue(long value) {
        this.value -= value;
    }

    @Override
    public void save() {
        transactionList.forEach(IStoreable::save);
        MySQLManager.getInstance().executeUpdate(
                String.format("INSERT INTO `%s` (id, IDOwner, Value, Name) VALUES ('%s','%s',%d, '%s') ON DUPLICATE KEY UPDATE Value = %d, Name='%s';",
                        TableCreationStrings.ACCOUNTS.getTableName(), this.id, this.ownerId, value, name, value, name)
        );
    }

    @Override
    public boolean load(String id) {
        this.id = id;
        ResultSet resultSet = MySQLManager.getInstance().executeQuery(String.format("SELECT * FROM `%s` WHERE id = '%s';",
                TableCreationStrings.ACCOUNTS.getTableName(), id
        ));
        try {
            if (resultSet.next()) {
                this.value = resultSet.getLong("Value");
                this.name = resultSet.getString("Name");
            }
            return true;
        } catch (SQLException throwables) {
            return false;
        }
    }

    @Override
    public Future<Boolean> delete() {
        String format = String.format("UPDATE `%s` SET IDOwner='%s' WHERE id='%s'", TableCreationStrings.ACCOUNTS.getTableName(), this.ownerId + "-deleted", this.id);
        return MySQLManager.getInstance().executeUpdate(format);
    }
}
