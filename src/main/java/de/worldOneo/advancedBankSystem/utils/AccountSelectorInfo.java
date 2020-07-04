package de.worldOneo.advancedBankSystem.utils;

import org.bukkit.entity.Player;

public class AccountSelectorInfo {
    public final boolean showValue;
    public final boolean showName;
    public final Player player;

    public AccountSelectorInfo(Player player, boolean showValue, boolean showName) {
        this.player = player;
        this.showValue = showValue;
        this.showName = showName;
    }
}
