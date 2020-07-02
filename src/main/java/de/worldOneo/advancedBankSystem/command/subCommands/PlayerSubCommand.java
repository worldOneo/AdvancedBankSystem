package de.worldOneo.advancedBankSystem.command.subCommands;

import org.bukkit.entity.Player;

public interface PlayerSubCommand {
    static PlayerSubCommand getInstance() {
        return (player, args) -> false;
    }

    boolean execute(Player player, String[] args);
}
