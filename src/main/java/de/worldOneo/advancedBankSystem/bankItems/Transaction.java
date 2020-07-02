package de.worldOneo.advancedBankSystem.bankItems;

import de.worldOneo.advancedBankSystem.manager.MySQLManager;
import de.worldOneo.advancedBankSystem.utils.TableCreationStrings;
import de.worldOneo.advancedBankSystem.utils.Utils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Transaction implements IBankItem, IStoreable {
    private final String id;
    private final String idFrom;
    private final String idTo;
    private final long value;
    private final long time;
    private final Future<Boolean> success;
    private final String reason;


    public Transaction(String idFrom, String idTo, long value, long time) {
        this(idFrom, idTo, value, time, "Unknown");
    }

    public Transaction(String idFrom, String idTo, long value, long time, String reason) {
        this.id = Utils.generateId();
        this.value = value;
        this.idFrom = idFrom;
        this.idTo = idTo;
        this.time = time;
        this.reason = reason;
        success = MySQLManager.getInstance().executeUpdate(
                String.format("INSERT IGNORE INTO `%s` (id, IDTo, IDFrom, Value, Time, Reason) VALUES ('%s', '%s', '%s', %d, %d, '%s');",
                        TableCreationStrings.TRANSACTIONS.getTableName(), id, idTo, idFrom, value, time, reason)
        );
    }

    public boolean isSuccess() {
        try {
            return success.isDone() ? success.get() : false;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public long getValue() {
        return value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getOwnerId() {
        return idFrom;
    }

    public String getIdTo() {
        return idTo;
    }

    public String getIdFrom() {
        return idFrom;
    }

    public String getReason() {
        return reason;
    }

    public long getTime() {
        return time;
    }

    public Future<Boolean> getSuccess() {
        return success;
    }

    @Override
    public void save() {

    }

    @Override
    public Future<Boolean> delete() {
        return null;
    }

    @Override
    public boolean load(String id) {
        return false;
    }
}
