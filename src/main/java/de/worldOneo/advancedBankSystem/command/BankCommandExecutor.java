package de.worldOneo.advancedBankSystem.command;

import de.worldOneo.advancedBankSystem.bankItems.IBankItem;
import de.worldOneo.advancedBankSystem.command.subCommands.CreateCommand;
import de.worldOneo.advancedBankSystem.command.subCommands.HelpCommand;
import de.worldOneo.advancedBankSystem.command.subCommands.PayCommand;
import de.worldOneo.advancedBankSystem.gui.BankGUI;
import de.worldOneo.advancedBankSystem.gui.InfoGUI;
import de.worldOneo.advancedBankSystem.manager.BankAccountManager;
import de.worldOneo.advancedBankSystem.manager.GUIManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BankCommandExecutor implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can work with the Bank!");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            GUIManager.getInstance().getGui(BankGUI.class).open(player, o -> {
            });
            return true;
        }
        switch (args[0]) {
            case "create":
                CreateCommand.getInstance().execute(player, args);
                break;
            case "pay":
                PayCommand.getInstance().execute(player, args);
                break;
            case "help":
                return HelpCommand.getInstance().execute(player, args);
            case "info":
                GUIManager.getInstance().getGui(InfoGUI.class).open(player, null);
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 0) {
            return Arrays.asList("help", "pay", "create", "info");
        }
        switch (args[0]) {
            case "create":
                return Arrays.asList("custom-name");
            case "help":
                return Arrays.asList("create", "pay", "info");
            case "pay":
                if (args.length == 2) {
                    return Bukkit.getOnlinePlayers().stream().map(Player::getDisplayName).collect(Collectors.toList());
                } else if (args.length == 3) {
                    Player player = Bukkit.getPlayer(args[1].trim());
                    if (player != null) {
                        return BankAccountManager.getInstance().getBankAccount(
                                player.getUniqueId().toString()
                        ).getAccounts().stream().map(IBankItem::getId).collect(Collectors.toList());
                    }
                    return null;
                } else if (args.length == 4) {
                    return Arrays.asList("amount");
                } else {
                    return Arrays.asList("reason");
                }
        }
        return Arrays.asList("help", "pay", "create", "info");
    }
}
