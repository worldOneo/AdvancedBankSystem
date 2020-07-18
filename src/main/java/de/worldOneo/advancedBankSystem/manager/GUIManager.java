package de.worldOneo.advancedBankSystem.manager;

import de.worldOneo.advancedBankSystem.gui.IGUI;
import de.worldOneo.advancedBankSystem.utils.Pair;
import de.worldOneo.advancedBankSystem.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Random;

public class GUIManager {
    private static final GUIManager instance = new GUIManager();
    private final HashMap<Player, Pair<String, IGUI>> playerIGUIMap = new HashMap<>();

    private GUIManager() {
    }

    public void open(IGUI iGUI) {
        Inventory inventory = iGUI.render();
        String title = iGUI.getGUITitle();
        title = generateID() + title;
        Inventory renamedInventory = Bukkit.createInventory(null, inventory.getSize(), title);
        renamedInventory.setContents(inventory.getContents());
        Player player = iGUI.getPlayer();
        playerIGUIMap.put(iGUI.getPlayer(), new Pair<>(title, iGUI));
        player.openInventory(renamedInventory);
    }

    public void handle(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        Pair<String, IGUI> pair = playerIGUIMap.get(player);
        if (pair == null) {
            return;
        }

        String title = pair.getKey();
        IGUI iGUI = pair.getValue();

        if (!title.equals(e.getView().getTitle())) return;

        iGUI.handle(e);
    }

    public static GUIManager getInstance() {
        return instance;
    }

    private String generateID() {
        char[] chars = "1234567890abcdeflnokm".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            stringBuilder.append("&").append(chars[random.nextInt(chars.length)]);
        }
        stringBuilder.append("&r");
        return Utils.colorize(stringBuilder.toString());
    }
}
