package de.worldOneo.advancedBankSystem.bankItems;

import org.bukkit.util.io.BukkitObjectOutputStream;

import java.util.concurrent.Future;

public interface IStoreable {
    void save();
    Future<Boolean> delete();
    boolean load(String id);
}
