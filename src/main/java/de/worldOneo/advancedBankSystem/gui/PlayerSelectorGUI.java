package de.worldOneo.advancedBankSystem.gui;

import de.worldOneo.advancedBankSystem.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class PlayerSelectorGUI extends AbstractInputGUI<Player> {
    private final ItemStack nextItem = Utils.getNamedItem(Material.ARROW, Utils.colorize("&a&rNext"));
    private final ItemStack backItem = Utils.getNamedItem(Material.ARROW, Utils.colorize("&a&rBack"));
    private int index = 0;
    private Player player;

    public PlayerSelectorGUI(Player player) {
        super(player);
    }

    @Override
    public boolean handle(InventoryClickEvent e) {
        if (!GUIUtils.isHandleable(e)) {
            return false;
        }
        if (e.getCurrentItem().equals(nextItem)) {
            index += 53;
            open();
        } else if (e.getCurrentItem().equals(backItem)) {
            index -= 53;
            open();
        } else if (e.getCurrentItem().getItemMeta() instanceof SkullMeta) {
            SkullMeta skullMeta = (SkullMeta) e.getCurrentItem().getItemMeta();
            this.player = (Player) skullMeta.getOwningPlayer();
            commitValue();
        }
        return super.handle(e);
    }

    @Override
    public Inventory render() {
        return createInventory(getPlayer());
    }

    public Inventory createInventory(Player player) {
        List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());
        int size = (playerList.stream().skip(index).count() > 54) ? 52 : 54;
        float inventorySize = Math.min(playerList.size(), 54);
        Inventory inventory = Bukkit.createInventory(null, (int) (Math.ceil(inventorySize / 9) * 9), getGUITitle());
        if (playerList.stream().skip(index).count() > 52) {
            inventory.setItem(53, nextItem);
        }
        if (index > 0) {
            inventory.setItem(45, backItem);
        }

        playerList.stream()
                .skip(index)
                .limit(size)
                .forEach(player1 -> {
                    ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
                    SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
                    skullMeta.setOwningPlayer(player1);
                    skullMeta.setDisplayName(player1.getDisplayName());
                    itemStack.setItemMeta(skullMeta);
                    inventory.addItem(itemStack);
                });

        return inventory;
    }

    @Override
    public String getGUITitle() {
        return "Select a player";
    }

    @Override
    public Player getValue() {
        return player;
    }
}
