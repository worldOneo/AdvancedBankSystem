package de.worldOneo.advancedBankSystem.command;

import de.worldOneo.advancedBankSystem.command.subCommands.CreateCommand;
import de.worldOneo.advancedBankSystem.command.subCommands.HelpCommand;
import de.worldOneo.advancedBankSystem.gui.BankGUI;
import de.worldOneo.advancedBankSystem.gui.InfoGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class BankCommandExecutor implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can work with the Bank!");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            BankGUI bankGUI = new BankGUI(player);
            bankGUI.open();
            return true;
        }
        switch (args[0]) {
            case "create":
                CreateCommand.getInstance().execute(player, args);
                break;
            case "help":
                return HelpCommand.getInstance().execute(player, args);
            case "info":
                InfoGUI infoGUI = new InfoGUI(player);
                infoGUI.open();
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 0) {
            return Arrays.asList("help", "create", "info");
        }
        switch (args[0]) {
            case "create":
                return Arrays.asList("custom-name");
            case "help":
                return Arrays.asList("create", "info");
        }
        return Arrays.asList("help", "create", "info");
    }
}
