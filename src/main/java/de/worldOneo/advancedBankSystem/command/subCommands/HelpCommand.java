package de.worldOneo.advancedBankSystem.command.subCommands;

import de.worldOneo.advancedBankSystem.BankSystem;
import org.bukkit.entity.Player;

public class HelpCommand implements PlayerSubCommand {
    private static final HelpCommand instance = new HelpCommand();

    private HelpCommand() {

    }

    public static HelpCommand getInstance() {
        return instance;
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length == 2) {
            String help = BankSystem.getInstance().getConfig().getString("help." + args[1].toLowerCase());
            if (help == null) {
                return false;
            }
            player.sendMessage(help.replaceFirst("<cmd>", "banksystem"));
            return true;
        }
        return false;
    }
}
