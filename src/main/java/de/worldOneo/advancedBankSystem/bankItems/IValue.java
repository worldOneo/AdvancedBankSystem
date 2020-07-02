package de.worldOneo.advancedBankSystem.bankItems;

public interface IValue {
    long getValue();

    void setValue(long value);

    void addValue(long value);

    void remValue(long value);
}
