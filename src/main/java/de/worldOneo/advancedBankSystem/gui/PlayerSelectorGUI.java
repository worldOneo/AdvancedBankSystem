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
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class PlayerSelectorGUI extends AbstractGUI {
    private final ItemStack nextItem = Utils.getNamedItem(Material.ARROW, Utils.colorize("&a&rNext"));
    private final ItemStack backItem = Utils.getNamedItem(Material.ARROW, Utils.colorize("&a&rBack"));
    private final HashMap<UUID, Integer> uuidIndexHashMap = new HashMap<>();
    private final HashMap<UUID, Consumer<Object>> uuidConsumerHashMap = new HashMap<>();

    @Override
    public IGUI getInstance() {
        return this;
    }

    @Override
    public boolean handle(InventoryClickEvent e) {
        if (!GUIUtils.isHandleable(e)) {
            return false;
        }
        if (e.getCurrentItem().equals(nextItem)) {
            uuidIndexHashMap.put(e.getWhoClicked().getUniqueId(), uuidIndexHashMap.get(e.getWhoClicked().getUniqueId()) + 53);
        } else if (e.getCurrentItem().equals(backItem)) {
            uuidIndexHashMap.put(e.getWhoClicked().getUniqueId(), uuidIndexHashMap.get(e.getWhoClicked().getUniqueId()) - 53);
        } else if (Bukkit.getPlayer(e.getCurrentItem().getItemMeta().getDisplayName()) != null) {
            uuidConsumerHashMap.get(e.getWhoClicked().getUniqueId()).accept(Bukkit.getPlayer(e.getCurrentItem().getItemMeta().getDisplayName()));
        }
        return super.handle(e);
    }

    @Override
    public void open(Player player, Consumer<Object> callback) {
        uuidIndexHashMap.put(player.getUniqueId(), 0);
        uuidConsumerHashMap.put(player.getUniqueId(), callback);
        player.openInventory(createInventory(player));
    }

    public Inventory createInventory(Player player) {
        List<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());
        int size = (playerList.stream().skip(uuidIndexHashMap.get(player.getUniqueId())).count() > 54) ? 52 : 54;
        float inventorySize = Math.min(playerList.size(), 54);
        Inventory inventory = Bukkit.createInventory(null, (int) (Math.ceil(inventorySize / 9) * 9), getGUITitle());
        if (playerList.stream().skip(uuidIndexHashMap.get(player.getUniqueId())).count() > 52) {
            inventory.setItem(53, nextItem);
        }
        if (uuidIndexHashMap.get(player.getUniqueId()) > 0) {
            inventory.setItem(45, backItem);
        }

        playerList.stream()
                .skip(uuidIndexHashMap.get(player.getUniqueId()))
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
        return Utils.colorize("&a&rSelect a player");
    }
}
