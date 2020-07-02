package de.worldOneo.advancedBankSystem.bankItems;

public interface IStoreable {
    void save();

    boolean load(String id);
}
