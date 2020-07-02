package de.worldOneo.advancedBankSystem.listeners;

import de.worldOneo.advancedBankSystem.BankSystem;
import de.worldOneo.advancedBankSystem.bankItems.BankAccount;
import de.worldOneo.advancedBankSystem.manager.BankAccountManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class OnPlayerConnectionEvent implements Listener {
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String id = e.getPlayer().getUniqueId().toString();
                BankAccount bankAccount = BankAccountManager.getInstance().createOrLoadAccount(id);
                BankAccountManager.getInstance().addAccount(bankAccount);
                e.getPlayer().sendMessage("Bank account loaded!");
            }
        }.runTaskAsynchronously(BankSystem.getInstance());


    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        saveRemovePlayerAccount(e.getPlayer().getUniqueId().toString());
    }

    @EventHandler
    public void onPlayerKickEvent(PlayerKickEvent e) {
        saveRemovePlayerAccount(e.getPlayer().getUniqueId().toString());
    }

    public void saveRemovePlayerAccount(String id) {
        BankAccountManager.getInstance().getBankAccount(id).save();
        BankAccountManager.getInstance().removeAccount(id);
    }
}
